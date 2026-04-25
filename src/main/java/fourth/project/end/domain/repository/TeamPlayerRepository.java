package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.TeamPlayer;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {

    List<TeamPlayer> findAllBySeasonTeamIdAndDeletedFalse(Long seasonTeamId);

    Optional<TeamPlayer> findBySeasonTeamIdAndPlayerIdAndDeletedFalse(Long seasonTeamId, Long playerId);

    boolean existsBySeasonTeamIdAndPlayerIdAndDeletedFalse(Long seasonTeamId, Long playerId);

    Optional<TeamPlayer> findByIdAndDeletedFalse(Long id);
}
