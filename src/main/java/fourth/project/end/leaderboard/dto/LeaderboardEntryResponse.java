package fourth.project.end.leaderboard.dto;

public record LeaderboardEntryResponse(
    Long userId,
    String displayName,
    Integer totalPoints,
    Integer rankPosition
) {
}
