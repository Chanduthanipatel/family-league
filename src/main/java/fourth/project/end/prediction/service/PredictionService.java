package fourth.project.end.prediction.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import fourth.project.end.common.exception.PredictionWindowClosedException;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.enums.PredictionStatus;
import fourth.project.end.domain.model.AppUser;
import fourth.project.end.domain.model.LeagueMatch;
import fourth.project.end.domain.model.LeaguePrediction;
import fourth.project.end.domain.model.LeaguePredictionItem;
import fourth.project.end.domain.model.MatchPrediction;
import fourth.project.end.domain.model.Player;
import fourth.project.end.domain.model.Season;
import fourth.project.end.domain.model.SeasonTeam;
import fourth.project.end.domain.model.Team;
import fourth.project.end.domain.repository.AppUserRepository;
import fourth.project.end.domain.repository.LeagueMatchRepository;
import fourth.project.end.domain.repository.LeaguePredictionItemRepository;
import fourth.project.end.domain.repository.LeaguePredictionRepository;
import fourth.project.end.domain.repository.MatchPredictionRepository;
import fourth.project.end.domain.repository.PlayerRepository;
import fourth.project.end.domain.repository.SeasonRepository;
import fourth.project.end.domain.repository.SeasonTeamRepository;
import fourth.project.end.domain.repository.TeamRepository;
import fourth.project.end.prediction.dto.LeaguePredictionEntryRequest;
import fourth.project.end.prediction.dto.LeaguePredictionEntryResponse;
import fourth.project.end.prediction.dto.LeaguePredictionResponse;
import fourth.project.end.prediction.dto.MatchPredictionResponse;
import fourth.project.end.prediction.dto.SaveLeaguePredictionRequest;
import fourth.project.end.prediction.dto.SaveMatchPredictionRequest;

@Service
public class PredictionService {

    private final SeasonRepository seasonRepository;
    private final SeasonTeamRepository seasonTeamRepository;
    private final LeaguePredictionRepository leaguePredictionRepository;
    private final LeaguePredictionItemRepository leaguePredictionItemRepository;
    private final LeagueMatchRepository leagueMatchRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final AppUserRepository appUserRepository;

    public PredictionService(
            SeasonRepository seasonRepository,
            SeasonTeamRepository seasonTeamRepository,
            LeaguePredictionRepository leaguePredictionRepository,
            LeaguePredictionItemRepository leaguePredictionItemRepository,
            LeagueMatchRepository leagueMatchRepository,
            MatchPredictionRepository matchPredictionRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            AppUserRepository appUserRepository) {
        this.seasonRepository = seasonRepository;
        this.seasonTeamRepository = seasonTeamRepository;
        this.leaguePredictionRepository = leaguePredictionRepository;
        this.leaguePredictionItemRepository = leaguePredictionItemRepository;
        this.leagueMatchRepository = leagueMatchRepository;
        this.matchPredictionRepository = matchPredictionRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public LeaguePredictionResponse saveLeaguePrediction(Long seasonId, SaveLeaguePredictionRequest request,
            UserPrincipal principal) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        enforceLeaguePredictionOpen(season);

        AppUser user = findUser(principal.getUserId());
        List<SeasonTeam> seasonTeams = seasonTeamRepository.findAllBySeasonIdAndDeletedFalse(seasonId);
        validateLeaguePredictionEntries(request.entries(), seasonTeams);

        Map<Long, SeasonTeam> seasonTeamById = seasonTeams.stream()
                .collect(Collectors.toMap(SeasonTeam::getId, Function.identity()));

        LeaguePrediction prediction = leaguePredictionRepository
                .findBySeasonIdAndUserIdAndDeletedFalse(seasonId, principal.getUserId())
                .orElseGet(LeaguePrediction::new);

        prediction.setSeason(season);
        prediction.setUser(user);
        prediction.setSubmittedAt(Instant.now());
        prediction.setLockedAtSnapshot(season.getLeaguePredictionLockAt());
        prediction.setStatus(PredictionStatus.SUBMITTED);
        LeaguePrediction savedPrediction = leaguePredictionRepository.save(prediction);

        leaguePredictionItemRepository.deleteAllByLeaguePredictionId(savedPrediction.getId());

        List<LeaguePredictionItem> items = request.entries().stream()
                .map(entry -> {
                    LeaguePredictionItem item = new LeaguePredictionItem();
                    item.setLeaguePrediction(savedPrediction);
                    item.setSeasonTeam(seasonTeamById.get(entry.seasonTeamId()));
                    item.setPredictedPosition(entry.predictedPosition());
                    return item;
                })
                .toList();
        leaguePredictionItemRepository.saveAll(items);

        return toLeaguePredictionResponse(savedPrediction, items);
    }

    @Transactional(readOnly = true)
    public LeaguePredictionResponse getOwnLeaguePrediction(Long seasonId, UserPrincipal principal) {
        LeaguePrediction prediction = leaguePredictionRepository
                .findBySeasonIdAndUserIdAndDeletedFalse(seasonId, principal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("League prediction not found"));
        List<LeaguePredictionItem> items = leaguePredictionItemRepository
                .findAllByLeaguePredictionIdAndDeletedFalseOrderByPredictedPositionAsc(prediction.getId());
        return toLeaguePredictionResponse(prediction, items);
    }

    @Transactional(readOnly = true)
    public List<LeaguePredictionResponse> listLeaguePredictionsAfterLock(Long seasonId) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));
        ensureLeaguePredictionsVisible(season);

        return leaguePredictionRepository.findAllBySeasonIdAndDeletedFalse(seasonId).stream()
                .map(prediction -> {
                    List<LeaguePredictionItem> items = leaguePredictionItemRepository
                            .findAllByLeaguePredictionIdAndDeletedFalseOrderByPredictedPositionAsc(prediction.getId());
                    return toLeaguePredictionResponse(prediction, items);
                })
                .toList();
    }

    @Transactional
    public MatchPredictionResponse saveMatchPrediction(Long matchId, SaveMatchPredictionRequest request,
            UserPrincipal principal) {
        LeagueMatch match = leagueMatchRepository.findByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        enforceMatchPredictionOpen(match);

        AppUser user = findUser(principal.getUserId());
        Team winnerTeam = validateMatchTeam(match, request.predictedWinnerTeamId(), "Predicted winner team");
        Team tossWinnerTeam = validateMatchTeam(match, request.predictedTossWinnerTeamId(),
                "Predicted toss winner team");
        Player player = playerRepository.findById(request.predictedPlayerOfMatchId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player not found: " + request.predictedPlayerOfMatchId()));

        MatchPrediction prediction = matchPredictionRepository
                .findByLeagueMatchIdAndUserIdAndDeletedFalse(matchId, principal.getUserId())
                .orElseGet(MatchPrediction::new);

        prediction.setLeagueMatch(match);
        prediction.setUser(user);
        prediction.setPredictedWinnerTeam(winnerTeam);
        prediction.setPredictedTossWinnerTeam(tossWinnerTeam);
        prediction.setPredictedPlayerOfMatch(player);
        prediction.setSubmittedAt(Instant.now());
        prediction.setLockedAtSnapshot(match.getPredictionLockAt());
        prediction.setStatus(PredictionStatus.SUBMITTED);

        return toMatchPredictionResponse(matchPredictionRepository.save(prediction));
    }

    @Transactional(readOnly = true)
    public MatchPredictionResponse getOwnMatchPrediction(Long matchId, UserPrincipal principal) {
        MatchPrediction prediction = matchPredictionRepository
                .findByLeagueMatchIdAndUserIdAndDeletedFalse(matchId, principal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Match prediction not found"));
        return toMatchPredictionResponse(prediction);
    }

    @Transactional(readOnly = true)
    public List<MatchPredictionResponse> listMatchPredictionsAfterLock(Long matchId) {
        LeagueMatch match = leagueMatchRepository.findByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));
        ensureMatchPredictionsVisible(match);

        return matchPredictionRepository.findAllByLeagueMatchIdAndDeletedFalse(matchId).stream()
                .map(this::toMatchPredictionResponse)
                .toList();
    }

    private void validateLeaguePredictionEntries(List<LeaguePredictionEntryRequest> entries,
            List<SeasonTeam> seasonTeams) {
        if (entries.size() != seasonTeams.size()) {
            throw new IllegalArgumentException("League prediction must cover the full season leaderboard");
        }

        Set<Long> validSeasonTeamIds = seasonTeams.stream().map(SeasonTeam::getId).collect(Collectors.toSet());
        Set<Long> seenSeasonTeams = new HashSet<>();
        Set<Integer> seenPositions = new HashSet<>();
        int expectedPositions = seasonTeams.size();

        for (LeaguePredictionEntryRequest entry : entries) {
            if (!validSeasonTeamIds.contains(entry.seasonTeamId())) {
                throw new IllegalArgumentException("Prediction contains a team that does not belong to this season");
            }
            if (entry.predictedPosition() > expectedPositions) {
                throw new IllegalArgumentException("Predicted position exceeds season team count");
            }
            if (!seenSeasonTeams.add(entry.seasonTeamId())) {
                throw new IllegalArgumentException("Duplicate season team in league prediction");
            }
            if (!seenPositions.add(entry.predictedPosition())) {
                throw new IllegalArgumentException("Duplicate predicted position in league prediction");
            }
        }
    }

    private AppUser findUser(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private Team validateMatchTeam(LeagueMatch match, Long teamId, String label) {
        Team team = teamRepository.findByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(label + " not found: " + teamId));
        if (!team.getId().equals(match.getHomeTeam().getId()) && !team.getId().equals(match.getAwayTeam().getId())) {
            throw new IllegalArgumentException(label + " must be one of the scheduled match teams");
        }
        return team;
    }

    private void enforceLeaguePredictionOpen(Season season) {
        Instant lockAt = season.getLeaguePredictionLockAt();
        if (lockAt == null) {
            throw new IllegalStateException(
                    "League prediction lock time is not configured for season " + season.getId());
        }
        if (!Instant.now().isBefore(lockAt)) {
            throw new PredictionWindowClosedException("League prediction window is closed");
        }
    }

    private void ensureLeaguePredictionsVisible(Season season) {
        Instant lockAt = season.getLeaguePredictionLockAt();
        if (lockAt == null || Instant.now().isBefore(lockAt)) {
            throw new PredictionWindowClosedException("Other users' league predictions are visible only after lock");
        }
    }

    private void enforceMatchPredictionOpen(LeagueMatch match) {
        if (!Instant.now().isBefore(match.getPredictionLockAt())) {
            throw new PredictionWindowClosedException("Match prediction window is closed");
        }
    }

    private void ensureMatchPredictionsVisible(LeagueMatch match) {
        if (Instant.now().isBefore(match.getPredictionLockAt())) {
            throw new PredictionWindowClosedException("Other users' match predictions are visible only after lock");
        }
    }

    private LeaguePredictionResponse toLeaguePredictionResponse(LeaguePrediction prediction,
            List<LeaguePredictionItem> items) {
        return new LeaguePredictionResponse(
                prediction.getId(),
                prediction.getSeason().getId(),
                prediction.getUser().getId(),
                prediction.getUser().getDisplayName(),
                prediction.getSubmittedAt(),
                prediction.getLockedAtSnapshot(),
                prediction.getStatus(),
                items.stream()
                        .map(item -> new LeaguePredictionEntryResponse(
                                item.getSeasonTeam().getId(),
                                item.getSeasonTeam().getTeam().getId(),
                                item.getSeasonTeam().getTeam().getCode(),
                                item.getSeasonTeam().getTeam().getName(),
                                item.getPredictedPosition()))
                        .toList());
    }

    private MatchPredictionResponse toMatchPredictionResponse(MatchPrediction prediction) {
        return new MatchPredictionResponse(
                prediction.getId(),
                prediction.getLeagueMatch().getId(),
                prediction.getUser().getId(),
                prediction.getUser().getDisplayName(),
                prediction.getPredictedWinnerTeam().getId(),
                prediction.getPredictedWinnerTeam().getName(),
                prediction.getPredictedTossWinnerTeam().getId(),
                prediction.getPredictedTossWinnerTeam().getName(),
                prediction.getPredictedPlayerOfMatch().getId(),
                prediction.getPredictedPlayerOfMatch().getFullName(),
                prediction.getSubmittedAt(),
                prediction.getLockedAtSnapshot(),
                prediction.getStatus());
    }
}
