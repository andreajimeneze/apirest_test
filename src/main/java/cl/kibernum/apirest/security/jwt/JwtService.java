package cl.kibernum.apirest.security.jwt;

// Entidades de dominio para roles y usuarios.
import cl.kibernum.apirest.security.domain.Role;
import cl.kibernum.apirest.security.domain.UserAccount;
// Librerías Nimbus JOSE + JWT para firmar y validar tokens.
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
// Logger para depuración de emisión de tokens.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Servicio responsable de generar y validar tokens JWT.
 *
 * Funciones principales:
 * - Generar access tokens y refresh tokens con claims estándar y personalizados.
 * - Validar tokens recibidos: firma, expiración, issuer, claims.
 * - Extraer payload relevante para el contexto de seguridad.
 *
 * Notas:
 * - Usa Nimbus JOSE + JWT para firmar (HS256) y validar tokens.
 * - El secreto debe tener al menos 32 caracteres (256 bits).
 * - Los claims incluyen: sub (usuario), roles, ver (tokenVersion), iss, iat, exp, jti.
 */
@Service
public class JwtService {
     // Logger para depuración de emisión de tokens.
     private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    // Propiedades de JWT y seguridad (secret, issuer, TTLs, toggles).
    private final JwtProperties props;
    // Reloj inyectable para facilitar pruebas y control de tiempo.
    private final Clock clock;

    // Constructor principal usado por Spring (inyecta propiedades y reloj del sistema).
    @Autowired
    public JwtService(JwtProperties props) {
        this(props, Clock.systemUTC());
    }

    // Constructor alternativo para pruebas (permite inyectar un Clock custom).
    public JwtService(JwtProperties props, Clock clock) {
        this.props = props;
        this.clock = clock;
    }

    /**
     * Genera un access token JWT para el usuario dado, con TTL configurado.
     * @param user usuario autenticado
     * @return JWT firmado listo para usar como Bearer token
     */
    public String generateAccessToken(UserAccount user) {
        return generateToken(user, props.getJwt().getAccessTtl().toSeconds());
    }

    /**
     * Genera un refresh token JWT para el usuario dado, si está habilitado.
     * @param user usuario autenticado
     * @return JWT firmado de refresco
     * @throws ResponseStatusException si los refresh tokens están deshabilitados
     */
    public String generateRefreshToken(UserAccount user) {
        if (!props.getJwt().isRefreshEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh tokens are disabled");
        }
        return generateToken(user, props.getJwt().getRefreshTtl().toSeconds());
    }

    /**
     * Lógica central para construir y firmar un JWT con claims estándar y personalizados.
     * @param user usuario autenticado
     * @param ttlSeconds tiempo de vida en segundos
     * @return JWT firmado
     */
    private String generateToken(UserAccount user, long ttlSeconds) {
        try {
            Instant now = clock.instant();
            List<String> roles = mapRoles(user.getRoles());
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(props.getJwt().getIssuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(ttlSeconds)))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("ver", user.getTokenVersion())
                .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
            SignedJWT jwt = new SignedJWT(header, claims);
            JWSSigner signer = new MACSigner(getSecret());
            jwt.sign(signer);
            String token = jwt.serialize();
            log.debug("Issued token for {}: {}...", user.getUsername(), token.substring(0, Math.min(10, token.length())));
            return token;
        } catch (JOSEException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not generate token");
        }
    }

    /**
     * Parsea y valida un JWT recibido: firma, expiración, issuer y claims.
     * @param token JWT recibido
     * @return JwtPayload con los datos extraídos y validados
     * @throws ResponseStatusException si el token es inválido o expirado
     */
    public JwtPayload parseAndValidate(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(getSecret());
            if (!jwt.verify(verifier)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token signature");
            }

            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            validateStandardClaims(claims);

            String subject = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("roles");
            Integer ver = claims.getIntegerClaim("ver");
            String jti = claims.getJWTID();

            return new JwtPayload(subject, roles, ver == null ? 0 : ver, jti, claims.getExpirationTime());
        } catch (ParseException | JOSEException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    /**
     * Valida los claims estándar: issuer y expiración (con 30s de tolerancia).
     * @param claims claims extraídos del JWT
     * @throws ResponseStatusException si el issuer es inválido o el token está expirado
     */
    private void validateStandardClaims(JWTClaimsSet claims) {
        Instant now = clock.instant();
        String issuer = claims.getIssuer();
        Date exp = claims.getExpirationTime();
        if (issuer == null || !issuer.equals(props.getJwt().getIssuer())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid issuer");
        }
        if (exp == null || exp.toInstant().isBefore(now.minusSeconds(30))) { // 30s clock skew
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }
    }

    /**
     * Obtiene el secreto de firma HMAC desde las propiedades y valida su longitud.
     * @return secreto de firma
     * @throws ResponseStatusException si el secreto es nulo o demasiado corto
     */
    private String getSecret() {
        String secret = props.getJwt().getSecret();
        if (secret == null || secret.length() < 32) { // HS256 requires 256-bit key
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT secret not configured properly");
        }
        return secret;
    }

    /**
     * Convierte el set de roles (enum) a lista de strings para el claim "roles".
     */
    private List<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(Role::name).collect(Collectors.toList());
    }

    /**
     * Contenedor simple para los datos extraídos y validados de un JWT.
     * Incluye: subject (usuario), roles, versión, jti y expiración.
     */
    public static class JwtPayload {
        private final String subject;
        private final List<String> roles;
        private final int version;
        private final String jti;
        private final Date expiresAt;

        public JwtPayload(String subject, List<String> roles, int version, String jti, Date expiresAt) {
            this.subject = subject;
            this.roles = roles;
            this.version = version;
            this.jti = jti;
            this.expiresAt = expiresAt;
        }

        public String getSubject() { return subject; }
        public List<String> getRoles() { return roles; }
        public int getVersion() { return version; }
        public String getJti() { return jti; }
        public Date getExpiresAt() { return expiresAt; }
    }
}