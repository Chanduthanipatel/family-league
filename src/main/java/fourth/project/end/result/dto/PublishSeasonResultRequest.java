package fourth.project.end.result.dto;

import java.util.List;

import fourth.project.end.domain.enums.SeasonResultStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PublishSeasonResultRequest(
    @NotNull SeasonResultStatus status,
    @NotEmpty List<@Valid SeasonResultItemRequest> items
) {
}
