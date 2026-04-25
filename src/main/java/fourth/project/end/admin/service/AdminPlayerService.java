package fourth.project.end.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.admin.dto.CreatePlayerRequest;
import fourth.project.end.admin.dto.PlayerResponse;
import fourth.project.end.admin.dto.UpdatePlayerRequest;
import fourth.project.end.common.exception.ConflictException;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.model.Player;
import fourth.project.end.domain.repository.PlayerRepository;

@Service
public class AdminPlayerService {

    private final PlayerRepository playerRepository;

    public AdminPlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public PlayerResponse createPlayer(CreatePlayerRequest request) {
        String code = request.code().toUpperCase().trim();
        if (playerRepository.existsByCodeAndDeletedFalse(code)) {
            throw new ConflictException("Player code already exists: " + code);
        }

        Player player = new Player();
        player.setCode(code);
        player.setFullName(request.fullName().trim());
        player.setShortName(request.shortName() != null ? request.shortName().trim() : null);
        player.setActive(true);
        return toPlayerResponse(playerRepository.save(player));
    }

    @Transactional(readOnly = true)
    public List<PlayerResponse> listPlayers(Boolean active) {
        if (active != null) {
            return playerRepository.findAllByActiveAndDeletedFalseOrderByFullNameAsc(active).stream()
                    .map(this::toPlayerResponse)
                    .toList();
        }
        return playerRepository.findAllByDeletedFalseOrderByFullNameAsc().stream()
                .map(this::toPlayerResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlayerResponse getPlayer(Long playerId) {
        return toPlayerResponse(findPlayer(playerId));
    }

    @Transactional
    public PlayerResponse updatePlayer(Long playerId, UpdatePlayerRequest request) {
        Player player = findPlayer(playerId);
        player.setFullName(request.fullName().trim());
        player.setShortName(request.shortName() != null ? request.shortName().trim() : null);
        player.setActive(request.active());
        return toPlayerResponse(playerRepository.save(player));
    }

    private Player findPlayer(Long playerId) {
        return playerRepository.findByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + playerId));
    }

    private PlayerResponse toPlayerResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getCode(),
                player.getFullName(),
                player.getShortName(),
                player.isActive());
    }
}
