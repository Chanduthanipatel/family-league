package fourth.project.end.result.dto;

import java.time.Instant;
import java.util.List;

import fourth.project.end.domain.enums.SeasonResultStatus;

public record PublishedSeasonResultResponse(
    Long resultId,
    Long seasonId,
    SeasonResultStatus status,
    Instant publishedAt,
    List<SeasonResultItemResponse> items,
    String note
) {
}
