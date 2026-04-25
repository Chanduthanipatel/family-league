package fourth.project.end.prediction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LeaguePredictionEntryRequest(
    @NotNull Long seasonTeamId,
    @NotNull @Min(1) Integer predictedPosition
) {
}
