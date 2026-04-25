package fourth.project.end.prediction.dto;

import jakarta.validation.constraints.NotNull;

public record SaveMatchPredictionRequest(
    @NotNull Long predictedWinnerTeamId,
    @NotNull Long predictedTossWinnerTeamId,
    @NotNull Long predictedPlayerOfMatchId
) {
}
