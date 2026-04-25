package fourth.project.end.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(
    @NotBlank @Size(max = 50) String code,
    @NotBlank @Size(max = 120) String name,
    @NotBlank @Size(max = 20) String shortName,
    @Size(max = 500) String logoUrl
) {
}
