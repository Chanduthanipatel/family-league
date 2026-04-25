package fourth.project.end.admin.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public record AssignPlayerToTeamRequest(
        @NotNull Long playerId,
        Instant joinedAt) {
}
