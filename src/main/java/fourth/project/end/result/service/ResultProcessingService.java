package fourth.project.end.result.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.enums.MatchResultType;
import fourth.project.end.domain.enums.MatchStatus;
import fourth.project.end.domain.enums.ScoreEventType;
import fourth.project.end.domain.enums.SeasonResultStatus;
import fourth.project.end.domain.enums.SeasonStatus;
import fourth.project.end.domain.model.AppUser;
import fourth.project.end.domain.model.LeaderboardEntry;
import fourth.project.end.domain.model.LeagueMatch;
import fourth.project.end.domain.model.MatchPrediction;
import fourth.project.end.domain.model.MatchResult;
import fourth.project.end.domain.model.Player;
import fourth.project.end.domain.model.ScoreEvent;
import fourth.project.end.domain.model.Season;
import fourth.project.end.domain.model.SeasonResult;
import fourth.project.end.domain.model.SeasonResultItem;
import fourth.project.end.domain.model.SeasonTeam;
import fourth.project.end.domain.model.Team;
import fourth.project.end.domain.repository.AppUserRepository;
import fourth.project.end.domain.repository.LeaderboardEntryRepository;
import fourth.project.end.domain.repository.LeagueMatchRepository;
import fourth.project.end.domain.repository.MatchPredictionRepository;
import fourth.project.end.domain.repository.MatchResultRepository;
import fourth.project.end.domain.repository.PlayerRepository;
import fourth.project.end.domain.repository.ScoreEventRepository;
import fourth.project.end.domain.repository.SeasonRepository;
import fourth.project.end.domain.repository.SeasonResultItemRepository;
import fourth.project.end.domain.repository.SeasonResultRepository;
import fourth.project.end.domain.repository.SeasonTeamRepository;
import fourth.project.end.domain.repository.TeamRepository;
import fourth.project.end.result.dto.PublishMatchResultRequest;
import fourth.project.end.result.dto.PublishSeasonResultRequest;
import fourth.project.end.result.dto.PublishedMatchResultResponse;
import fourth.project.end.result.dto.PublishedSeasonResultResponse;
import fourth.project.end.result.dto.SeasonResultItemRequest;
import fourth.project.end.result.dto.SeasonResultItemResponse;

@Service
public class ResultProcessingService {

    private static final String LEAGUE_SCORING_NOTE =
        "Season standings are stored, but league-level prediction scoring is not applied yet.";

    private final LeagueMatchRepository leagueMatchRepository;
    private final MatchResultRepository matchResultRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final ScoreEventRepository scoreEventRepository;
    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonResultRepository seasonResultRepository;
    private final SeasonResultItemRepository seasonResultItemRepository;
    private final SeasonTeamRepository seasonTeamRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final AppUserRepository appUserRepository;

    public ResultProcessingService(
        LeagueMatchRepository leagueMatchRepository,
        MatchResultRepository matchResultRepository,
        MatchPredictionRepository matchPredictionRepository,
        ScoreEventRepository scoreEventRepository,
        LeaderboardEntryRepository leaderboardEntryRepository,
        SeasonRepository seasonRepository,
        SeasonResultRepository seasonResultRepository,
        SeasonResultItemRepository seasonResultItemRepository,
        SeasonTeamRepository seasonTeamRepository,
        TeamRepository teamRepository,
        PlayerRepository playerRepository,
        AppUserRepository appUserRepository
    ) {
        this.leagueMatchRepository = leagueMatchRepository;
        this.matchResultRepository = matchResultRepository;
        this.matchPredictionRepository = matchPredictionRepository;
        this.scoreEventRepository = scoreEventRepository;
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.seasonRepository = seasonRepository;
        this.seasonResultRepository = seasonResultRepository;
        this.seasonResultItemRepository = seasonResultItemRepository;
        this.seasonTeamRepository = seasonTeamRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public PublishedMatchResultResponse publishMatchResult(
        Long matchId,
        PublishMatchResultRequest request,
        UserPrincipal principal
    ) {
        LeagueMatch match = leagueMatchRepository.findByIdAndDeletedFalse(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        validateMatchResultRequest(match, request);

        AppUser publishedBy = appUserRepository.findById(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + principal.getUserId()));

        MatchResult result = matchResultRepository.findByLeagueMatchIdAndDeletedFalse(matchId)
            .orElseGet(MatchResult::new);

        result.setLeagueMatch(match);
        result.setWinningTeam(resolveTeam(match, request.winningTeamId()));
        result.setTossWinnerTeam(resolveTeam(match, request.tossWinnerTeamId()));
        result.setPlayerOfMatch(resolvePlayer(request.playerOfMatchId()));
        result.setResultType(request.resultType());
        result.setPublishedBy(publishedBy);
        result.setPublishedAt(Instant.now());
        result.setRemarks(trimToNull(request.remarks()));
        MatchResult savedResult = matchResultRepository.save(result);

        match.setStatus(MatchStatus.RESULT_PUBLISHED);

        rebuildScoreEventsForMatch(match, savedResult);
        rebuildLeaderboard(match.getSeason().getId());

        return toPublishedMatchResultResponse(savedResult);
    }

    @Transactional(readOnly = true)
    public PublishedMatchResultResponse getMatchResult(Long matchId) {
        MatchResult result = matchResultRepository.findByLeagueMatchIdAndDeletedFalse(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match result not found"));
        return toPublishedMatchResultResponse(result);
    }

    @Transactional
    public PublishedSeasonResultResponse publishSeasonResult(
        Long seasonId,
        PublishSeasonResultRequest request,
        UserPrincipal principal
    ) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        AppUser publishedBy = appUserRepository.findById(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + principal.getUserId()));

        List<SeasonTeam> seasonTeams = seasonTeamRepository.findAllBySeasonIdAndDeletedFalse(seasonId);
        validateSeasonResultItems(request.items(), seasonTeams);

        Map<Long, SeasonTeam> seasonTeamById = seasonTeams.stream()
            .collect(Collectors.toMap(SeasonTeam::getId, Function.identity()));

        SeasonResult seasonResult = seasonResultRepository.findBySeasonIdAndDeletedFalse(seasonId)
            .orElseGet(SeasonResult::new);
        seasonResult.setSeason(season);
        seasonResult.setPublishedBy(publishedBy);
        seasonResult.setPublishedAt(Instant.now());
        seasonResult.setStatus(request.status());
        SeasonResult savedSeasonResult = seasonResultRepository.save(seasonResult);

        seasonResultItemRepository.deleteAllBySeasonResultId(savedSeasonResult.getId());

        List<SeasonResultItem> items = request.items().stream()
            .map(itemRequest -> {
                SeasonResultItem item = new SeasonResultItem();
                item.setSeasonResult(savedSeasonResult);
                item.setSeasonTeam(seasonTeamById.get(itemRequest.seasonTeamId()));
                item.setFinalPosition(itemRequest.finalPosition());
                item.setPoints(itemRequest.points());
                item.setWins(itemRequest.wins());
                item.setLosses(itemRequest.losses());
                item.setTies(itemRequest.ties());
                return item;
            })
            .toList();
        seasonResultItemRepository.saveAll(items);

        season.setStatus(SeasonStatus.COMPLETED);

        return toPublishedSeasonResultResponse(savedSeasonResult, items);
    }

    @Transactional(readOnly = true)
    public PublishedSeasonResultResponse getSeasonResult(Long seasonId) {
        SeasonResult seasonResult = seasonResultRepository.findBySeasonIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season result not found"));
        List<SeasonResultItem> items = seasonResultItemRepository
            .findAllBySeasonResultIdAndDeletedFalseOrderByFinalPositionAsc(seasonResult.getId());
        return toPublishedSeasonResultResponse(seasonResult, items);
    }

    private void rebuildScoreEventsForMatch(LeagueMatch match, MatchResult result) {
        scoreEventRepository.deleteAllByLeagueMatchId(match.getId());

        List<MatchPrediction> predictions = matchPredictionRepository.findAllByLeagueMatchIdAndDeletedFalse(match.getId());
        List<ScoreEvent> events = new ArrayList<>();

        for (MatchPrediction prediction : predictions) {
            if (isWinnerCorrect(result, prediction)) {
                events.add(createScoreEvent(match, prediction.getUser(), ScoreEventType.MATCH_WINNER));
            }
            if (isTossCorrect(result, prediction)) {
                events.add(createScoreEvent(match, prediction.getUser(), ScoreEventType.TOSS_WINNER));
            }
            if (isPlayerOfMatchCorrect(result, prediction)) {
                events.add(createScoreEvent(match, prediction.getUser(), ScoreEventType.PLAYER_OF_MATCH));
            }
        }

        if (!events.isEmpty()) {
            scoreEventRepository.saveAll(events);
        }
    }

    private void rebuildLeaderboard(Long seasonId) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found for leaderboard: " + seasonId));
        leaderboardEntryRepository.deleteAllBySeasonId(seasonId);

        Map<Long, Integer> pointsByUser = new LinkedHashMap<>();
        scoreEventRepository.findAllBySeasonIdAndDeletedFalse(seasonId).forEach(event ->
            pointsByUser.merge(event.getUser().getId(), event.getPointsAwarded(), Integer::sum)
        );

        List<Map.Entry<Long, Integer>> rankedUsers = pointsByUser.entrySet().stream()
            .sorted(
                Map.Entry.<Long, Integer>comparingByValue(Comparator.reverseOrder())
                    .thenComparing(Map.Entry.comparingByKey())
            )
            .toList();

        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        int rank = 1;
        Integer previousPoints = null;
        int previousRank = 1;

        for (int index = 0; index < rankedUsers.size(); index++) {
            Map.Entry<Long, Integer> entry = rankedUsers.get(index);
            AppUser user = appUserRepository.findById(entry.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + entry.getKey()));

            if (previousPoints != null && !previousPoints.equals(entry.getValue())) {
                previousRank = index + 1;
            }
            rank = previousRank;

            LeaderboardEntry leaderboardEntry = new LeaderboardEntry();
            leaderboardEntry.setSeason(season);
            leaderboardEntry.setUser(user);
            leaderboardEntry.setTotalPoints(entry.getValue());
            leaderboardEntry.setRankPosition(rank);
            leaderboardEntry.setLastRecalculatedAt(Instant.now());
            leaderboardEntries.add(leaderboardEntry);

            previousPoints = entry.getValue();
        }

        if (!leaderboardEntries.isEmpty()) {
            leaderboardEntryRepository.saveAll(leaderboardEntries);
        }
    }

    private ScoreEvent createScoreEvent(LeagueMatch match, AppUser user, ScoreEventType eventType) {
        ScoreEvent event = new ScoreEvent();
        event.setSeason(match.getSeason());
        event.setUser(user);
        event.setLeagueMatch(match);
        event.setEventType(eventType);
        event.setPointsAwarded(1);
        event.setSourceRef("MATCH_RESULT:" + match.getId());
        event.setProcessedAt(Instant.now());
        return event;
    }

    private boolean isWinnerCorrect(MatchResult result, MatchPrediction prediction) {
        if (result.getResultType() == MatchResultType.NO_RESULT || result.getWinningTeam() == null) {
            return false;
        }
        return prediction.getPredictedWinnerTeam() != null
            && prediction.getPredictedWinnerTeam().getId().equals(result.getWinningTeam().getId());
    }

    private boolean isTossCorrect(MatchResult result, MatchPrediction prediction) {
        return result.getTossWinnerTeam() != null
            && prediction.getPredictedTossWinnerTeam() != null
            && prediction.getPredictedTossWinnerTeam().getId().equals(result.getTossWinnerTeam().getId());
    }

    private boolean isPlayerOfMatchCorrect(MatchResult result, MatchPrediction prediction) {
        return result.getPlayerOfMatch() != null
            && prediction.getPredictedPlayerOfMatch() != null
            && prediction.getPredictedPlayerOfMatch().getId().equals(result.getPlayerOfMatch().getId());
    }

    private Team resolveTeam(LeagueMatch match, Long teamId) {
        if (teamId == null) {
            return null;
        }
        Team team = teamRepository.findByIdAndDeletedFalse(teamId)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + teamId));
        if (!team.getId().equals(match.getHomeTeam().getId()) && !team.getId().equals(match.getAwayTeam().getId())) {
            throw new IllegalArgumentException("Result team must belong to the scheduled match");
        }
        return team;
    }

    private Player resolvePlayer(Long playerId) {
        if (playerId == null) {
            return null;
        }
        return playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + playerId));
    }

    private void validateMatchResultRequest(LeagueMatch match, PublishMatchResultRequest request) {
        if (request.resultType() == MatchResultType.NORMAL || request.resultType() == MatchResultType.TIE) {
            if (request.winningTeamId() == null) {
                throw new IllegalArgumentException("Winning team is required for normal and tie results");
            }
        }
        if (request.resultType() == MatchResultType.NO_RESULT && request.winningTeamId() != null) {
            throw new IllegalArgumentException("Winning team must be null when result type is NO_RESULT");
        }
        if (request.tossWinnerTeamId() != null) {
            resolveTeam(match, request.tossWinnerTeamId());
        }
        if (request.winningTeamId() != null) {
            resolveTeam(match, request.winningTeamId());
        }
        if (request.playerOfMatchId() != null) {
            resolvePlayer(request.playerOfMatchId());
        }
    }

    private void validateSeasonResultItems(List<SeasonResultItemRequest> items, List<SeasonTeam> seasonTeams) {
        if (items.size() != seasonTeams.size()) {
            throw new IllegalArgumentException("Season result must cover the full season leaderboard");
        }

        Set<Long> validSeasonTeamIds = seasonTeams.stream().map(SeasonTeam::getId).collect(Collectors.toSet());
        Set<Long> seenSeasonTeams = new HashSet<>();
        Set<Integer> seenPositions = new HashSet<>();
        int expectedPositions = seasonTeams.size();

        for (SeasonResultItemRequest item : items) {
            if (!validSeasonTeamIds.contains(item.seasonTeamId())) {
                throw new IllegalArgumentException("Season result contains a team that does not belong to this season");
            }
            if (item.finalPosition() > expectedPositions) {
                throw new IllegalArgumentException("Final position exceeds season team count");
            }
            if (!seenSeasonTeams.add(item.seasonTeamId())) {
                throw new IllegalArgumentException("Duplicate season team in season result");
            }
            if (!seenPositions.add(item.finalPosition())) {
                throw new IllegalArgumentException("Duplicate final position in season result");
            }
        }
    }

    private PublishedMatchResultResponse toPublishedMatchResultResponse(MatchResult result) {
        return new PublishedMatchResultResponse(
            result.getId(),
            result.getLeagueMatch().getId(),
            result.getLeagueMatch().getSeason().getId(),
            result.getWinningTeam() == null ? null : result.getWinningTeam().getId(),
            result.getWinningTeam() == null ? null : result.getWinningTeam().getName(),
            result.getTossWinnerTeam() == null ? null : result.getTossWinnerTeam().getId(),
            result.getTossWinnerTeam() == null ? null : result.getTossWinnerTeam().getName(),
            result.getPlayerOfMatch() == null ? null : result.getPlayerOfMatch().getId(),
            result.getPlayerOfMatch() == null ? null : result.getPlayerOfMatch().getFullName(),
            result.getResultType(),
            result.getRemarks(),
            result.getPublishedAt()
        );
    }

    private PublishedSeasonResultResponse toPublishedSeasonResultResponse(SeasonResult seasonResult, List<SeasonResultItem> items) {
        return new PublishedSeasonResultResponse(
            seasonResult.getId(),
            seasonResult.getSeason().getId(),
            seasonResult.getStatus(),
            seasonResult.getPublishedAt(),
            items.stream()
                .sorted(Comparator.comparing(SeasonResultItem::getFinalPosition))
                .map(item -> new SeasonResultItemResponse(
                    item.getSeasonTeam().getId(),
                    item.getSeasonTeam().getTeam().getId(),
                    item.getSeasonTeam().getTeam().getCode(),
                    item.getSeasonTeam().getTeam().getName(),
                    item.getFinalPosition(),
                    item.getPoints(),
                    item.getWins(),
                    item.getLosses(),
                    item.getTies()
                ))
                .toList(),
            LEAGUE_SCORING_NOTE
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
