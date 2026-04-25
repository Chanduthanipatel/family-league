package fourth.project.end.leaderboard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.repository.LeaderboardEntryRepository;
import fourth.project.end.domain.repository.SeasonRepository;
import fourth.project.end.leaderboard.dto.LeaderboardEntryResponse;
import fourth.project.end.leaderboard.dto.LeaderboardResponse;

@Service
public class LeaderboardService {

    private final SeasonRepository seasonRepository;
    private final LeaderboardEntryRepository leaderboardEntryRepository;

    public LeaderboardService(
        SeasonRepository seasonRepository,
        LeaderboardEntryRepository leaderboardEntryRepository
    ) {
        this.seasonRepository = seasonRepository;
        this.leaderboardEntryRepository = leaderboardEntryRepository;
    }

    @Transactional(readOnly = true)
    public LeaderboardResponse getSeasonLeaderboard(Long seasonId) {
        seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        List<LeaderboardEntryResponse> entries = leaderboardEntryRepository
            .findAllBySeasonIdAndDeletedFalseOrderByRankPositionAsc(seasonId).stream()
            .map(entry -> new LeaderboardEntryResponse(
                entry.getUser().getId(),
                entry.getUser().getDisplayName(),
                entry.getTotalPoints(),
                entry.getRankPosition()
            ))
            .toList();

        return new LeaderboardResponse(seasonId, entries);
    }
}
