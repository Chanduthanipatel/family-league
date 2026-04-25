package fourth.project.end.leaderboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.leaderboard.dto.LeaderboardResponse;
import fourth.project.end.leaderboard.service.LeaderboardService;

@RestController
@RequestMapping("/api/v1/leaderboards")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/seasons/{seasonId}")
    public LeaderboardResponse getSeasonLeaderboard(@PathVariable Long seasonId) {
        return leaderboardService.getSeasonLeaderboard(seasonId);
    }
}
