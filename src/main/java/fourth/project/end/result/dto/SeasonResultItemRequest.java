package fourth.project.end.result.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SeasonResultItemRequest(
    @NotNull Long seasonTeamId,
    @NotNull @Min(1) Integer finalPosition,
    Integer points,
    Integer wins,
    Integer losses,
    Integer ties
) {
}
