package fourth.project.end.prediction.dto;

import java.time.Instant;

import fourth.project.end.domain.enums.PredictionStatus;

public record MatchPredictionResponse(
    Long predictionId,
    Long matchId,
    Long userId,
    String displayName,
    Long predictedWinnerTeamId,
    String predictedWinnerTeamName,
    Long predictedTossWinnerTeamId,
    String predictedTossWinnerTeamName,
    Long predictedPlayerOfMatchId,
    String predictedPlayerOfMatchName,
    Instant submittedAt,
    Instant lockedAtSnapshot,
    PredictionStatus status
) {
}
