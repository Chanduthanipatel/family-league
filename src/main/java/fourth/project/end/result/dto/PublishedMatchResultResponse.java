package fourth.project.end.result.dto;

import java.time.Instant;

import fourth.project.end.domain.enums.MatchResultType;

public record PublishedMatchResultResponse(
    Long resultId,
    Long matchId,
    Long seasonId,
    Long winningTeamId,
    String winningTeamName,
    Long tossWinnerTeamId,
    String tossWinnerTeamName,
    Long playerOfMatchId,
    String playerOfMatchName,
    MatchResultType resultType,
    String remarks,
    Instant publishedAt
) {
}
