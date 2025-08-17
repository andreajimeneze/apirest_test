package cl.kibernum.apirest.security.dto;

public class AuthResponse {
     // JWT de acceso firmado (Bearer token) para usar en Authorization: Bearer <token>.
    private String accessToken;
     // Tiempo de vida (TTL) del accessToken expresado en segundos.
    private long expiresIn;
     // Token de refresco, opcional. Puede venir null cuando refresh está deshabilitado.
    private String refreshToken;

     // Constructor por defecto requerido por Jackson.
    public AuthResponse() {}

     /**
      * Crea una respuesta de autenticación con los valores emitidos por el servicio JWT.
      * @param accessToken token de acceso firmado
      * @param expiresIn segundos de validez del accessToken
      * @param refreshToken token de refresco (o null si no aplica)
      */
    public AuthResponse(String accessToken, long expiresIn, String refreshToken) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

     // Getters y setters necesarios para la serialización JSON.
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
