package fourth.project.end.admin.dto;

import java.time.Instant;

public record TeamPlayerResponse(
        Long id,
        Long seasonTeamId,
        Long teamId,
        String teamName,
        Long playerId,
        String playerCode,
        String playerFullName,
        String playerShortName,
        Instant joinedAt,
        Instant leftAt) {
}
