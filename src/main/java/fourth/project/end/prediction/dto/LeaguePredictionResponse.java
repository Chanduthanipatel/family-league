package fourth.project.end.prediction.dto;

import java.time.Instant;
import java.util.List;

import fourth.project.end.domain.enums.PredictionStatus;

public record LeaguePredictionResponse(
    Long predictionId,
    Long seasonId,
    Long userId,
    String displayName,
    Instant submittedAt,
    Instant lockedAtSnapshot,
    PredictionStatus status,
    List<LeaguePredictionEntryResponse> entries
) {
}
