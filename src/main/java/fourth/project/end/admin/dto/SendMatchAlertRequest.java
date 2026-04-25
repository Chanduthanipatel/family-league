package fourth.project.end.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendMatchAlertRequest(
        @NotNull Long matchId,
        @NotBlank String customMessage) {
}
