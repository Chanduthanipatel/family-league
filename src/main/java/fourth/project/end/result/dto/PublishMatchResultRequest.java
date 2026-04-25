package fourth.project.end.result.dto;

import fourth.project.end.domain.enums.MatchResultType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublishMatchResultRequest(
    Long winningTeamId,
    Long tossWinnerTeamId,
    Long playerOfMatchId,
    @NotNull MatchResultType resultType,
    @Size(max = 5000) String remarks
) {
}
