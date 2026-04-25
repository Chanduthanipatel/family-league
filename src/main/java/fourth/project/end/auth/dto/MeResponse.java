package fourth.project.end.auth.dto;

import java.util.List;

public record MeResponse(
    Long userId,
    String email,
    String displayName,
    List<String> roles
) {
}
