package cl.kibernum.apirest.security.controller;

// Roles de la aplicación (ROLE_USER, ROLE_ADMIN).
import cl.kibernum.apirest.security.domain.Role;
// Entidad JPA que representa a los usuarios de la aplicación.
import cl.kibernum.apirest.security.domain.UserAccount;
// Respuesta de autenticación con tokens y expiración.
import cl.kibernum.apirest.security.dto.AuthResponse;
// DTO para login (username/password) con validación.
import cl.kibernum.apirest.security.dto.LoginRequest;
// DTO para registro (username/password) con validación.
import cl.kibernum.apirest.security.dto.RegisterRequest;
// Propiedades de JWT (secret, issuer, TTLs, toggles, CORS...).
import cl.kibernum.apirest.security.jwt.JwtProperties;
// Servicio para emitir y validar tokens JWT.
import cl.kibernum.apirest.security.jwt.JwtService;
// Repositorio para persistencia de usuarios.
import cl.kibernum.apirest.security.repository.UserAccountRepository;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador apirest de autenticación y gestión de tokens.
 *
 * Endpoints expuestos (todos bajo /api/auth):
 * - POST /login: autentica un usuario (username/password) y devuelve
 * accessToken (+ refreshToken si está habilitado).
 * - POST /register: registra un nuevo usuario con ROLE_USER si la
 * característica está habilitada en propiedades.
 * - POST /refresh: recibe un refresh token (en el cuerpo como texto plano) y
 * devuelve un nuevo accessToken (+ refreshToken).
 *
 * Respuestas y errores:
 * - 200 OK: operación exitosa.
 * - 401 Unauthorized: credenciales inválidas o refresh token inválido/expirado.
 * - 404 Not Found: registro/refresh deshabilitados por configuración.
 * - 400 Bad Request: datos inválidos (manejado por validación y advice global).
 */
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    // Gestor de autenticación configurado por Spring (usa nuestro
    // UserDetailsService + PasswordEncoder).
    private final AuthenticationManager authManager;
    // Servicio JWT para generar y validar tokens.
    private final JwtService jwtService;
    // Repositorio de usuarios para consultas y persistencia.
    private final UserAccountRepository userRepo;
    // Codificador de contraseñas (BCrypt).
    private final PasswordEncoder passwordEncoder;
    // Propiedades de seguridad, incluyendo toggles de registro/refresh.
    private final JwtProperties props;

    // Inyección por constructor de todos los colaboradores.
    public AuthController(AuthenticationManager authManager, JwtService jwtService,
            UserAccountRepository userRepo, PasswordEncoder passwordEncoder,
            JwtProperties props) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
    }

    /**
     * Autentica a un usuario mediante username/password y retorna tokens.
     * Entrada: {@link LoginRequest} validado (@Valid).
     * Salida: {@link AuthResponse} con accessToken, expiresIn y (opcional)
     * refreshToken.
     * Errores: 401 (BadCredentialsException) si credenciales inválidas.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Delegamos la verificación de credenciales al AuthenticationManager
            // (PasswordEncoder incluido).
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            // Cargamos el usuario para construir los claims/roles del token.
            UserAccount user = userRepo.findByUsername(request.getUsername()).orElseThrow();
            // Generamos access token (siempre) y refresh token (si está habilitado).
            String access = jwtService.generateAccessToken(user);
            String refresh = props.getJwt().isRefreshEnabled() ? jwtService.generateRefreshToken(user) : null;
            long expiresIn = props.getJwt().getAccessTtl().toSeconds();
            return ResponseEntity.ok(new AuthResponse(access, expiresIn, refresh));
        } catch (BadCredentialsException ex) {
            // Propagamos para que nuestro handler de seguridad formatee la respuesta 401 en
            // JSON.
            throw ex;
        }
    }

    /**
     * Registra un nuevo usuario como ROLE_USER.
     * Requiere que security.auth.registration-enabled=true.
     * Retorna 200 OK si se crea; 404 si la característica está deshabilitada.
     * Lanza IllegalArgumentException si el username ya existe (manejado por advice
     * global).
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        if (!props.getAuth().isRegistrationEnabled()) {
            return ResponseEntity.notFound().build();
        }
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Construcción y persistencia de un nuevo usuario con contraseña cifrada y rol
        // USER.
        UserAccount ua = new UserAccount();
        ua.setUsername(request.getUsername());
        ua.setNombre(request.getNombre());
        ua.setApellido(request.getApellido());
        ua.setEmail(request.getEmail());
        ua.setPassword(passwordEncoder.encode(request.getPassword()));
        ua.setEnabled(true);
        ua.setRoles(Set.of(Role.ROLE_USER));
        userRepo.save(ua);
        return ResponseEntity.ok().build();
    }

    /**
     * Emite nuevos tokens a partir de un refresh token válido.
     * Importante: este endpoint espera el refresh token en el cuerpo como texto
     * plano (no JSON).
     * Si refresh está deshabilitado via propiedades, responde 404 Not Found.
     * Valida la versión del token (tokenVersion) para soportar invalidación global
     * por usuario.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        if (!props.getJwt().isRefreshEnabled()) {
            return ResponseEntity.notFound().build();
        }
        // Valida firma, expiración, issuer y extrae claims.
        var payload = jwtService.parseAndValidate(refreshToken);
        UserAccount user = userRepo.findByUsername(payload.getSubject()).orElseThrow();
        // Compara la versión de token embebida con la versión actual del usuario.
        if (payload.getVersion() != user.getTokenVersion()) {
            throw new BadCredentialsException("Invalid refresh token version");
        }
        // Genera nuevo access token y, por conveniencia, un nuevo refresh token rotado.
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        long expiresIn = props.getJwt().getAccessTtl().toSeconds();
        return ResponseEntity.ok(new AuthResponse(access, expiresIn, refresh));
    }
}
