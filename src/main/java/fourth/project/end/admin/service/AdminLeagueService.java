package fourth.project.end.admin.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.admin.dto.CreateLeagueRequest;
import fourth.project.end.admin.dto.CreateSeasonRequest;
import fourth.project.end.admin.dto.LeagueResponse;
import fourth.project.end.admin.dto.SeasonResponse;
import fourth.project.end.admin.dto.UpdateSeasonStatusRequest;
import fourth.project.end.common.exception.ConflictException;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.enums.SeasonStatus;
import fourth.project.end.domain.model.League;
import fourth.project.end.domain.model.Season;
import fourth.project.end.domain.repository.LeagueRepository;
import fourth.project.end.domain.repository.SeasonRepository;

@Service
public class AdminLeagueService {

    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;

    public AdminLeagueService(LeagueRepository leagueRepository, SeasonRepository seasonRepository) {
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
    }

    @Transactional
    public LeagueResponse createLeague(CreateLeagueRequest request) {
        if (leagueRepository.existsByCodeAndDeletedFalse(request.code())) {
            throw new ConflictException("League code already exists: " + request.code());
        }

        League league = new League();
        league.setCode(normalize(request.code()));
        league.setName(request.name().trim());
        league.setDescription(trimToNull(request.description()));
        return toLeagueResponse(leagueRepository.save(league));
    }

    @Transactional(readOnly = true)
    public List<LeagueResponse> listLeagues() {
        return leagueRepository.findAllByDeletedFalseOrderByNameAsc().stream()
            .map(this::toLeagueResponse)
            .toList();
    }

    @Transactional
    public SeasonResponse createSeason(Long leagueId, CreateSeasonRequest request) {
        League league = leagueRepository.findByIdAndDeletedFalse(leagueId)
            .orElseThrow(() -> new ResourceNotFoundException("League not found: " + leagueId));

        if (seasonRepository.existsByCodeAndDeletedFalse(request.code())) {
            throw new ConflictException("Season code already exists: " + request.code());
        }

        validateSeasonDates(request.firstMatchAt(), request.leaguePredictionLockAt());

        Season season = new Season();
        season.setLeague(league);
        season.setCode(normalize(request.code()));
        season.setName(request.name().trim());
        season.setSeasonYear(request.seasonYear());
        season.setStatus(request.status() == null ? SeasonStatus.DRAFT : request.status());
        season.setStartsAt(request.startsAt());
        season.setEndsAt(request.endsAt());
        season.setFirstMatchAt(request.firstMatchAt());
        season.setLeaguePredictionLockAt(request.leaguePredictionLockAt());
        return toSeasonResponse(seasonRepository.save(season));
    }

    @Transactional(readOnly = true)
    public List<SeasonResponse> listSeasons(Long leagueId) {
        leagueRepository.findByIdAndDeletedFalse(leagueId)
            .orElseThrow(() -> new ResourceNotFoundException("League not found: " + leagueId));

        return seasonRepository.findAllByLeagueIdAndDeletedFalse(leagueId).stream()
            .map(this::toSeasonResponse)
            .toList();
    }

    @Transactional
    public SeasonResponse updateSeasonStatus(Long seasonId, UpdateSeasonStatusRequest request) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        season.setStatus(request.status());
        if (request.status() == SeasonStatus.CLOSED) {
            season.setClosedAt(Instant.now());
        } else if (season.getClosedAt() != null) {
            season.setClosedAt(null);
        }
        return toSeasonResponse(season);
    }

    private void validateSeasonDates(Instant firstMatchAt, Instant leaguePredictionLockAt) {
        if (firstMatchAt != null && leaguePredictionLockAt != null && leaguePredictionLockAt.isAfter(firstMatchAt)) {
            throw new IllegalArgumentException("League prediction lock time must be before or equal to first match time");
        }
    }

    private LeagueResponse toLeagueResponse(League league) {
        return new LeagueResponse(
            league.getId(),
            league.getCode(),
            league.getName(),
            league.getDescription()
        );
    }

    private SeasonResponse toSeasonResponse(Season season) {
        return new SeasonResponse(
            season.getId(),
            season.getLeague().getId(),
            season.getCode(),
            season.getName(),
            season.getSeasonYear(),
            season.getStatus(),
            season.getStartsAt(),
            season.getEndsAt(),
            season.getFirstMatchAt(),
            season.getLeaguePredictionLockAt(),
            season.getClosedAt()
        );
    }

    private String normalize(String value) {
        return value.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
