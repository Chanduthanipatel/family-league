package fourth.project.end.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePlayerRequest(
        @NotBlank @Size(max = 150) String fullName,
        @Size(max = 80) String shortName,
        boolean active) {
}
