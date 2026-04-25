package fourth.project.end.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePlayerRequest(
        @NotBlank @Size(max = 50) String code,
        @NotBlank @Size(max = 150) String fullName,
        @Size(max = 80) String shortName) {
}
