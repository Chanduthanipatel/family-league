package fourth.project.end.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLeagueRequest(
    @NotBlank @Size(max = 50) String code,
    @NotBlank @Size(max = 120) String name,
    @Size(max = 5000) String description
) {
}
