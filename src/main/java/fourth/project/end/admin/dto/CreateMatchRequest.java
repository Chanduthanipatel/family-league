package fourth.project.end.admin.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMatchRequest(
    Integer matchNumber,
    @NotNull Long homeTeamId,
    @NotNull Long awayTeamId,
    @Size(max = 255) String venue,
    @NotNull Instant startsAt,
    @NotNull Instant predictionLockAt
) {
}
