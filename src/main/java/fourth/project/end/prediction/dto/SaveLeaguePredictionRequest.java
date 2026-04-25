package fourth.project.end.prediction.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SaveLeaguePredictionRequest(
    @NotEmpty List<@Valid LeaguePredictionEntryRequest> entries
) {
}
