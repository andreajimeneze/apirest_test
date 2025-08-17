package cl.kibernum.apirest.security.config;


// Manejador centralizado para respuestas 401/403 en formato JSON.
import cl.kibernum.apirest.security.exception.SecurityExceptionHandler;
// Filtro que valida el JWT en cada request y establece la autenticación en el contexto.
import cl.kibernum.apirest.security.filter.JwtAuthenticationFilter;
// Propiedades externas de seguridad (secret, issuer, TTLs, CORS, etc.).
import cl.kibernum.apirest.security.jwt.JwtProperties;
import java.time.Duration;
import java.util.List;
// Anotaciones de configuración y definición de beans.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import cl.kibernum.apirest.security.repository.UserAccountRepository;

/**
 * Configuración principal de Spring Security.
 *
 * Objetivos clave:
 * - Autenticación stateless con JWT (sin sesiones de servidor).
 * - Reglas de autorización por método HTTP y ruta (rol USER/ADMIN).
 * - CORS configurable desde propiedades.
 * - Manejo consistente de errores 401/403.
 * - Compatibilidad con H2 console en desarrollo.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Filtro que procesa el header Authorization: Bearer <token>.
    private final JwtAuthenticationFilter jwtFilter;
    // Propiedades (security.jwt.*, security.cors.*, etc.).
    private final JwtProperties props;
    // EntryPoint/AccessDeniedHandler personalizados para 401 y 403.
    private final SecurityExceptionHandler securityExceptionHandler;

    // Inyección por constructor de dependencias necesarias para la configuración.
    public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtProperties props, SecurityExceptionHandler securityExceptionHandler) {
        this.jwtFilter = jwtFilter;
        this.props = props;
        this.securityExceptionHandler = securityExceptionHandler;
    }

    // Encoder de contraseñas. BCrypt es un estándar seguro y recomendado.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expone el AuthenticationManager que construye Spring a partir de la configuración.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Cadena de filtros y reglas de seguridad HTTP.
     * - Desactiva CSRF porque trabajamos con tokens (no cookies).
     * - Sesión STATELESS: no se guarda estado del usuario en servidor.
     * - CORS según configuración.
     * - Manejo de errores con nuestro handler.
     * - Reglas de autorización para /api/books y apertura de /api/auth/** y H2.
     * - Inserta el filtro JWT antes del de username/password.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(securityExceptionHandler)
                .accessDeniedHandler(securityExceptionHandler)
            )
            // Permite iframes para poder abrir la consola H2 en dev.
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // for H2 console
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (login/registro/refresh y consola H2).
                .requestMatchers("/api/v1/auth/**", "/h2-console/**").permitAll()
                // Lecturas de libros permitidas a USER o ADMIN.
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").hasAnyRole("ADMIN")
                // Operaciones de escritura solo para ADMIN.
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Carga de usuarios desde la base de datos (tabla users + user_roles).
     * Usa authorities con valores del enum (ROLE_*) para evitar doble prefijo.
     */
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository repo) {
        return username -> repo.findByUsername(username)
            .map(ua -> User.withUsername(ua.getUsername())
                .password(ua.getPassword())
                .authorities(ua.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .disabled(!ua.isEnabled())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // No se declara AuthenticationProvider manual para evitar APIs deprecadas.
    // Spring Boot configurará DaoAuthenticationProvider automáticamente usando este
    // UserDetailsService y el PasswordEncoder declarado arriba.

    /**
     * Configuración de CORS:
     * - allowedOrigins: toma la lista desde propiedades (security.cors.allowed-origins),
     *   o usa patrón abierto (*) si no está definida.
     * - allowedHeaders y methods: todos.
     * - credenciales: false (para tokens Bearer no son necesarias cookies).
     * - maxAge: 1 hora para cachear preflight.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = props.getCors().getAllowedOrigins();
        if (origins != null && !origins.isEmpty()) {
            config.setAllowedOrigins(origins);
        } else {
            config.addAllowedOriginPattern("*");
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(false);
        config.setMaxAge(Duration.ofHours(1));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
