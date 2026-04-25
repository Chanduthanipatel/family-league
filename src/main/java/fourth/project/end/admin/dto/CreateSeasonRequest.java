package fourth.project.end.admin.dto;

import java.time.Instant;

import fourth.project.end.domain.enums.SeasonStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSeasonRequest(
    @NotBlank @Size(max = 50) String code,
    @NotBlank @Size(max = 120) String name,
    @NotNull @Min(2000) @Max(3000) Integer seasonYear,
    SeasonStatus status,
    Instant startsAt,
    Instant endsAt,
    Instant firstMatchAt,
    Instant leaguePredictionLockAt
) {
}
