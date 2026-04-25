package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.SeasonTeam;

public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, Long> {

    List<SeasonTeam> findAllBySeasonIdAndDeletedFalse(Long seasonId);

    Optional<SeasonTeam> findBySeasonIdAndTeamIdAndDeletedFalse(Long seasonId, Long teamId);

    Optional<SeasonTeam> findByIdAndDeletedFalse(Long id);
}
