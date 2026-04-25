package fourth.project.end.admin.dto;

public record SeasonTeamResponse(
    Long id,
    Long seasonId,
    Long teamId,
    String teamCode,
    String teamName,
    String shortName,
    Integer seededPosition
) {
}
