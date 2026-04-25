package fourth.project.end.auth.config;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(buildJsonBody(
            HttpStatus.UNAUTHORIZED,
            "Authentication is required",
            request.getRequestURI()
        ));
    }

    private String buildJsonBody(HttpStatus status, String message, String path) {
        return """
            {"timestamp":"%s","status":%d,"error":"%s","message":"%s","path":"%s"}
            """.formatted(
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            status.value(),
            escape(status.getReasonPhrase()),
            escape(message),
            escape(path)
        );
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
