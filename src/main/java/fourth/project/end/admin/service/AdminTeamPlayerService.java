package fourth.project.end.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.admin.dto.AssignPlayerToTeamRequest;
import fourth.project.end.admin.dto.TeamPlayerResponse;
import fourth.project.end.common.exception.ConflictException;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.model.Player;
import fourth.project.end.domain.model.SeasonTeam;
import fourth.project.end.domain.model.TeamPlayer;
import fourth.project.end.domain.repository.PlayerRepository;
import fourth.project.end.domain.repository.SeasonTeamRepository;
import fourth.project.end.domain.repository.TeamPlayerRepository;

@Service
public class AdminTeamPlayerService {

    private final SeasonTeamRepository seasonTeamRepository;
    private final PlayerRepository playerRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    public AdminTeamPlayerService(
            SeasonTeamRepository seasonTeamRepository,
            PlayerRepository playerRepository,
            TeamPlayerRepository teamPlayerRepository) {
        this.seasonTeamRepository = seasonTeamRepository;
        this.playerRepository = playerRepository;
        this.teamPlayerRepository = teamPlayerRepository;
    }

    @Transactional
    public TeamPlayerResponse assignPlayerToSeasonTeam(Long seasonTeamId, AssignPlayerToTeamRequest request) {
        SeasonTeam seasonTeam = seasonTeamRepository.findByIdAndDeletedFalse(seasonTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Season team not found: " + seasonTeamId));

        Player player = playerRepository.findByIdAndDeletedFalse(request.playerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + request.playerId()));

        if (teamPlayerRepository.existsBySeasonTeamIdAndPlayerIdAndDeletedFalse(seasonTeamId, player.getId())) {
            throw new ConflictException("Player " + player.getCode() + " is already assigned to this season team");
        }

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setSeasonTeam(seasonTeam);
        teamPlayer.setPlayer(player);
        teamPlayer.setJoinedAt(request.joinedAt());

        return toResponse(teamPlayerRepository.save(teamPlayer));
    }

    @Transactional(readOnly = true)
    public List<TeamPlayerResponse> listPlayersInSeasonTeam(Long seasonTeamId) {
        seasonTeamRepository.findByIdAndDeletedFalse(seasonTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Season team not found: " + seasonTeamId));

        return teamPlayerRepository.findAllBySeasonTeamIdAndDeletedFalse(seasonTeamId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void removePlayerFromSeasonTeam(Long seasonTeamId, Long playerId) {
        TeamPlayer teamPlayer = teamPlayerRepository
                .findBySeasonTeamIdAndPlayerIdAndDeletedFalse(seasonTeamId, playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Player " + playerId + " is not assigned to season team " + seasonTeamId));

        teamPlayer.setDeleted(true);
        teamPlayerRepository.save(teamPlayer);
    }

    private TeamPlayerResponse toResponse(TeamPlayer tp) {
        return new TeamPlayerResponse(
                tp.getId(),
                tp.getSeasonTeam().getId(),
                tp.getSeasonTeam().getTeam().getId(),
                tp.getSeasonTeam().getTeam().getName(),
                tp.getPlayer().getId(),
                tp.getPlayer().getCode(),
                tp.getPlayer().getFullName(),
                tp.getPlayer().getShortName(),
                tp.getJoinedAt(),
                tp.getLeftAt());
    }
}
