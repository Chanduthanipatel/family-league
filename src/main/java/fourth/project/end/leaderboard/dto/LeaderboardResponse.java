package fourth.project.end.leaderboard.dto;

import java.util.List;

public record LeaderboardResponse(
    Long seasonId,
    List<LeaderboardEntryResponse> entries
) {
}
