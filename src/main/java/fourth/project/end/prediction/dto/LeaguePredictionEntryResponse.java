package fourth.project.end.prediction.dto;

public record LeaguePredictionEntryResponse(
    Long seasonTeamId,
    Long teamId,
    String teamCode,
    String teamName,
    Integer predictedPosition
) {
}
