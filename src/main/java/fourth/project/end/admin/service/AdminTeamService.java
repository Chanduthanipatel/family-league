package fourth.project.end.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.admin.dto.AssignSeasonTeamRequest;
import fourth.project.end.admin.dto.CreateTeamRequest;
import fourth.project.end.admin.dto.SeasonTeamResponse;
import fourth.project.end.admin.dto.TeamResponse;
import fourth.project.end.common.exception.ConflictException;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.model.Season;
import fourth.project.end.domain.model.SeasonTeam;
import fourth.project.end.domain.model.Team;
import fourth.project.end.domain.repository.SeasonRepository;
import fourth.project.end.domain.repository.SeasonTeamRepository;
import fourth.project.end.domain.repository.TeamRepository;

@Service
public class AdminTeamService {

    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonTeamRepository seasonTeamRepository;

    public AdminTeamService(
        TeamRepository teamRepository,
        SeasonRepository seasonRepository,
        SeasonTeamRepository seasonTeamRepository
    ) {
        this.teamRepository = teamRepository;
        this.seasonRepository = seasonRepository;
        this.seasonTeamRepository = seasonTeamRepository;
    }

    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request) {
        if (teamRepository.existsByCodeAndDeletedFalse(request.code())) {
            throw new ConflictException("Team code already exists: " + request.code());
        }

        Team team = new Team();
        team.setCode(request.code().trim().toUpperCase());
        team.setName(request.name().trim());
        team.setShortName(request.shortName().trim().toUpperCase());
        team.setLogoUrl(trimToNull(request.logoUrl()));
        return toTeamResponse(teamRepository.save(team));
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> listTeams() {
        return teamRepository.findAllByDeletedFalseOrderByNameAsc().stream()
            .map(this::toTeamResponse)
            .toList();
    }

    @Transactional
    public SeasonTeamResponse assignTeamToSeason(Long seasonId, AssignSeasonTeamRequest request) {
        Season season = seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        Team team = teamRepository.findByIdAndDeletedFalse(request.teamId())
            .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + request.teamId()));

        if (seasonTeamRepository.findBySeasonIdAndTeamIdAndDeletedFalse(seasonId, request.teamId()).isPresent()) {
            throw new ConflictException("Team is already assigned to season");
        }

        SeasonTeam seasonTeam = new SeasonTeam();
        seasonTeam.setSeason(season);
        seasonTeam.setTeam(team);
        seasonTeam.setSeededPosition(request.seededPosition());
        return toSeasonTeamResponse(seasonTeamRepository.save(seasonTeam));
    }

    @Transactional(readOnly = true)
    public List<SeasonTeamResponse> listSeasonTeams(Long seasonId) {
        seasonRepository.findByIdAndDeletedFalse(seasonId)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        return seasonTeamRepository.findAllBySeasonIdAndDeletedFalse(seasonId).stream()
            .map(this::toSeasonTeamResponse)
            .toList();
    }

    private TeamResponse toTeamResponse(Team team) {
        return new TeamResponse(
            team.getId(),
            team.getCode(),
            team.getName(),
            team.getShortName(),
            team.getLogoUrl()
        );
    }

    private SeasonTeamResponse toSeasonTeamResponse(SeasonTeam seasonTeam) {
        return new SeasonTeamResponse(
            seasonTeam.getId(),
            seasonTeam.getSeason().getId(),
            seasonTeam.getTeam().getId(),
            seasonTeam.getTeam().getCode(),
            seasonTeam.getTeam().getName(),
            seasonTeam.getTeam().getShortName(),
            seasonTeam.getSeededPosition()
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
