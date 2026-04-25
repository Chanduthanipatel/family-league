package fourth.project.end.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.admin.dto.CreateMatchRequest;
import fourth.project.end.admin.dto.MatchResponse;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.model.LeagueMatch;
import fourth.project.end.domain.model.Season;
import fourth.project.end.domain.model.SeasonTeam;
import fourth.project.end.domain.repository.LeagueMatchRepository;
import fourth.project.end.domain.repository.SeasonRepository;
import fourth.project.end.domain.repository.SeasonTeamRepository;

@Service
public class AdminMatchService {

    private final SeasonRepository seasonRepository;
    private final SeasonTeamRepository seasonTeamRepository;
    private final LeagueMatchRepository leagueMatchRepository;

    public AdminMatchService(
        SeasonRepository seasonRepository,
        SeasonTeamRepository seasonTeamRepository,
        LeagueMatchRepository leagueMatchRepository
    ) {
        this.seasonRepository = seasonRepository;
        this.seasonTeamRepository = seasonTeamRepository;
        this.leagueMatchRepository = leagueMatchRepository;
    }

    @Transactional
    public MatchResponse createMatch(Long seasonId, CreateMatchRequest request) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        if (request.homeTeamId().equals(request.awayTeamId())) {
            throw new IllegalArgumentException("Home and away teams must be different");
        }
        if (request.predictionLockAt().isAfter(request.startsAt())) {
            throw new IllegalArgumentException("Prediction lock time must be before or equal to match start time");
        }

        SeasonTeam homeSeasonTeam = seasonTeamRepository.findBySeasonIdAndTeamIdAndDeletedFalse(seasonId, request.homeTeamId())
            .orElseThrow(() -> new ResourceNotFoundException("Home team is not assigned to season"));
        SeasonTeam awaySeasonTeam = seasonTeamRepository.findBySeasonIdAndTeamIdAndDeletedFalse(seasonId, request.awayTeamId())
            .orElseThrow(() -> new ResourceNotFoundException("Away team is not assigned to season"));

        LeagueMatch leagueMatch = new LeagueMatch();
        leagueMatch.setSeason(season);
        leagueMatch.setHomeTeam(homeSeasonTeam.getTeam());
        leagueMatch.setAwayTeam(awaySeasonTeam.getTeam());
        leagueMatch.setMatchNumber(request.matchNumber());
        leagueMatch.setVenue(trimToNull(request.venue()));
        leagueMatch.setStartsAt(request.startsAt());
        leagueMatch.setPredictionLockAt(request.predictionLockAt());

        return toMatchResponse(leagueMatchRepository.save(leagueMatch));
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> listMatches(Long seasonId) {
        seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        return leagueMatchRepository.findAllBySeasonIdAndDeletedFalseOrderByStartsAtAsc(seasonId).stream()
            .map(this::toMatchResponse)
            .toList();
    }

    private MatchResponse toMatchResponse(LeagueMatch match) {
        return new MatchResponse(
            match.getId(),
            match.getSeason().getId(),
            match.getMatchNumber(),
            match.getHomeTeam().getId(),
            match.getHomeTeam().getName(),
            match.getAwayTeam().getId(),
            match.getAwayTeam().getName(),
            match.getVenue(),
            match.getStartsAt(),
            match.getPredictionLockAt(),
            match.getStatus()
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
