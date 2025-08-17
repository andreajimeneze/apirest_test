package cl.kibernum.apirest.security.jwt;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
public class JwtProperties {
     // Subgrupo de propiedades para endpoints de autenticación (registro).
     private Auth auth = new Auth();
     // Subgrupo de propiedades para JWT (secret, issuer, TTLs, toggles).
     private Jwt jwt = new Jwt();
     // Subgrupo de propiedades para CORS (orígenes permitidos).
     private Cors cors = new Cors();

    // Getters para inyección y acceso desde otros beans.
    public Auth getAuth() { return auth; }
    public Jwt getJwt() { return jwt; }
    public Cors getCors() { return cors; }

    /**
     * Subgrupo de propiedades para endpoints de autenticación (registro de usuarios).
     */
    public static class Auth {
        // Habilita/deshabilita el endpoint de registro público.
        private boolean registrationEnabled = false;
        public boolean isRegistrationEnabled() { return registrationEnabled; }
        public void setRegistrationEnabled(boolean registrationEnabled) { this.registrationEnabled = registrationEnabled; }
    }

    /**
     * Subgrupo de propiedades para configuración de JWT.
     */
    public static class Jwt {
        // Secreto de firma HMAC para los tokens JWT (debe ser largo y seguro).
        private String secret;
        // Issuer (emisor) que se incluye y valida en los tokens.
        private String issuer = "apirest";
        // Tiempo de vida del access token (ej: 15 minutos).
        private Duration accessTtl = Duration.ofMinutes(15);
        // Tiempo de vida del refresh token (ej: 7 días).
        private Duration refreshTtl = Duration.ofDays(7);
        // Habilita/deshabilita el endpoint y emisión de refresh tokens.
        private boolean refreshEnabled = true;
        // Habilita/deshabilita la funcionalidad de denylist (no implementada por defecto).
        private boolean denylistEnabled = false;

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }
        public Duration getAccessTtl() { return accessTtl; }
        public void setAccessTtl(Duration accessTtl) { this.accessTtl = accessTtl; }
        public Duration getRefreshTtl() { return refreshTtl; }
        public void setRefreshTtl(Duration refreshTtl) { this.refreshTtl = refreshTtl; }
        public boolean isRefreshEnabled() { return refreshEnabled; }
        public void setRefreshEnabled(boolean refreshEnabled) { this.refreshEnabled = refreshEnabled; }
        public boolean isDenylistEnabled() { return denylistEnabled; }
        public void setDenylistEnabled(boolean denylistEnabled) { this.denylistEnabled = denylistEnabled; }
    }

    /**
     * Subgrupo de propiedades para configuración de CORS.
     */
    public static class Cors {
        // Lista de orígenes permitidos para CORS (ej: ["http://localhost:3000"]).
        private List<String> allowedOrigins;
        public List<String> getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(List<String> allowedOrigins) { this.allowedOrigins = allowedOrigins; }
    }
}
