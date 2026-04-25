package fourth.project.end.result.dto;

public record SeasonResultItemResponse(
    Long seasonTeamId,
    Long teamId,
    String teamCode,
    String teamName,
    Integer finalPosition,
    Integer points,
    Integer wins,
    Integer losses,
    Integer ties
) {
}
