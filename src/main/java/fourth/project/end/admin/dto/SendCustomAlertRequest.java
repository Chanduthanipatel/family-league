package fourth.project.end.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record SendCustomAlertRequest(
        @NotBlank String subject,
        @NotBlank String body) {
}
