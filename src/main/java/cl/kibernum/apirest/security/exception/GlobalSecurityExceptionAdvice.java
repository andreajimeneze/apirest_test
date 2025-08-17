package cl.kibernum.apirest.security.exception;


// Para obtener la URI de la request que causó la excepción.
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Advice global para manejar excepciones de seguridad/autenticación y devolver respuestas JSON uniformes.
 *
 * Objetivo:
 * - Interceptar excepciones comunes de seguridad y validación para endpoints de autenticación.
 * - Formatear la respuesta como JSON con campos estándar: error, code, timestamp, path.
 *
 * Ejemplo de respuesta:
 * {
 *   "error": "Bad credentials",
 *   "code": 401,
 *   "timestamp": "2025-08-12T12:34:56.789Z",
 *   "path": "/api/auth/login"
 * }
 */
@RestControllerAdvice
public class GlobalSecurityExceptionAdvice {


    /**
     * Maneja errores de autenticación (credenciales inválidas).
     * Devuelve 401 Unauthorized con mensaje y detalles de la request.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return error(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
    }


    /**
     * Maneja errores de validación de argumentos (@Valid en DTOs).
     * Devuelve 400 Bad Request con mensaje genérico y detalles de la request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        return error(HttpStatus.BAD_REQUEST, "Validation error", req.getRequestURI());
    }

    /**
     * Construye la respuesta JSON estándar para cualquier error manejado.
     * @param status código HTTP a devolver
     * @param message mensaje de error
     * @param path URI de la request que causó el error
     * @return ResponseEntity con cuerpo JSON y status adecuado
     */
    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("code", status.value());
        body.put("timestamp", Instant.now().toString());
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }
}
