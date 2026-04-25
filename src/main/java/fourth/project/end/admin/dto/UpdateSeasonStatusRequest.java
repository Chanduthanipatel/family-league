package fourth.project.end.admin.dto;

import fourth.project.end.domain.enums.SeasonStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSeasonStatusRequest(
    @NotNull SeasonStatus status
) {
}
