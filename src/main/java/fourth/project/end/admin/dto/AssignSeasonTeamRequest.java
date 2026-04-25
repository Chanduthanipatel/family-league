package fourth.project.end.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssignSeasonTeamRequest(
    @NotNull Long teamId,
    @Min(1) Integer seededPosition
) {
}
