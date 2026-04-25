package fourth.project.end.admin.dto;

import java.time.Instant;

import fourth.project.end.domain.enums.SeasonStatus;

public record SeasonResponse(
    Long id,
    Long leagueId,
    String code,
    String name,
    Integer seasonYear,
    SeasonStatus status,
    Instant startsAt,
    Instant endsAt,
    Instant firstMatchAt,
    Instant leaguePredictionLockAt,
    Instant closedAt
) {
}
