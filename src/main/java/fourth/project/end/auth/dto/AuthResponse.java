package fourth.project.end.auth.dto;

import java.util.List;

public record AuthResponse(
    String accessToken,
    String tokenType,
    long expiresInSeconds,
    Long userId,
    String email,
    String displayName,
    List<String> roles
) {
}
