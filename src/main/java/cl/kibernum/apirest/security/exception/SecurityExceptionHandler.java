package cl.kibernum.apirest.security.exception;


// Serializador JSON para escribir la respuesta de error.
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Handler centralizado para errores de seguridad (401 y 403) que responde siempre en formato JSON.
 *
 * Implementa:
 * - AuthenticationEntryPoint: para errores de autenticación (no autenticado, token inválido, etc.).
 * - AccessDeniedHandler: para errores de autorización (falta de permisos, rol insuficiente).
 *
 * El formato de respuesta es uniforme:
 * {
 *   "error": "Unauthorized" | "Forbidden",
 *   "code": 401 | 403,
 *   "timestamp": "2025-08-12T12:34:56.789Z",
 *   "path": "/api/books/1"
 * }
 */
@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    // Serializador JSON de Jackson para escribir el cuerpo de la respuesta.
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Maneja errores de autenticación (no autenticado, token inválido, etc.).
     * Devuelve 401 Unauthorized en formato JSON.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {
        writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", request.getRequestURI());
    }


    /**
     * Maneja errores de autorización (rol insuficiente, acceso denegado).
     * Devuelve 403 Forbidden en formato JSON.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {
        writeJson(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", request.getRequestURI());
    }

    /**
     * Escribe la respuesta de error en formato JSON con los campos estándar.
     * @param response HttpServletResponse donde escribir
     * @param status código HTTP (401 o 403)
     * @param error mensaje de error ("Unauthorized" o "Forbidden")
     * @param path URI de la request que causó el error
     */
    private void writeJson(HttpServletResponse response, int status, String error, String path) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new HashMap<>();
        body.put("error", error);
        body.put("code", status);
        body.put("timestamp", Instant.now().toString());
        body.put("path", path);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
