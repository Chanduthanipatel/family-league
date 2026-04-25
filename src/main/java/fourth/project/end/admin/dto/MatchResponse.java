package fourth.project.end.admin.dto;

import java.time.Instant;

import fourth.project.end.domain.enums.MatchStatus;

public record MatchResponse(
    Long id,
    Long seasonId,
    Integer matchNumber,
    Long homeTeamId,
    String homeTeamName,
    Long awayTeamId,
    String awayTeamName,
    String venue,
    Instant startsAt,
    Instant predictionLockAt,
    MatchStatus status
) {
}
