package cl.kibernum.apirest.security.filter;

// Servicio para validar y decodificar tokens JWT.

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
// Constantes estándar de headers HTTP.
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cl.kibernum.apirest.security.jwt.JwtService;

/**
 * Filtro que autentica las peticiones HTTP usando tokens JWT (Bearer).
 *
 * Flujo:
 * - Intercepta cada request (OncePerRequestFilter).
 * - Si el header Authorization comienza con "Bearer ", intenta validar el token.
 * - Si es válido, extrae el usuario y roles, y los coloca en el SecurityContext.
 * - Si es inválido, responde 401 Unauthorized en JSON y detiene la cadena de filtros.
 * - Si no hay token, deja pasar la request (puede ser endpoint público).
 *
 * Nota: Este filtro se inserta antes del UsernamePasswordAuthenticationFilter en la cadena de Spring Security.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio para validar y decodificar tokens JWT.
    private final JwtService jwtService;

    // Inyección por constructor del servicio JWT.
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Procesa cada request entrante:
     * - Si hay header Authorization con Bearer, valida el token.
     * - Si es válido, setea el usuario y roles en el contexto de seguridad.
     * - Si es inválido, responde 401 y detiene la cadena.
     * - Si no hay token, deja pasar la request.
     *
     * @param request petición HTTP entrante
     * @param response respuesta HTTP saliente
     * @param filterChain cadena de filtros de Spring Security
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        // Extrae el header Authorization.
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                // Valida firma, expiración y claims del token.
                var payload = jwtService.parseAndValidate(token);
                // Convierte los roles a authorities de Spring Security.
                var authorities = payload.getRoles().stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
                // Crea el objeto Authentication y lo coloca en el contexto.
                Authentication auth = new UsernamePasswordAuthenticationToken(payload.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                // Si el token es inválido, responde 401 Unauthorized en JSON y detiene la cadena.
                response.resetBuffer();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized\",\"code\":401}");
                response.flushBuffer();
                return; // stop chain
            }
        }
        // Si no hay token, o es endpoint público, continúa la cadena normalmente.
        filterChain.doFilter(request, response);
    }

    /**
     * Evita aplicar el filtro a endpoints públicos como /api/auth/** o la consola H2.
     * Esto previene que un Authorization inválido cause 401 en rutas públicas (por ejemplo, /api/auth/login).
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/v1/auth/") || uri.startsWith("/h2-console");
    }
}
