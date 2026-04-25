package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.enums.SeasonStatus;
import fourth.project.end.domain.model.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    Optional<Season> findByCodeAndDeletedFalse(String code);

    Optional<Season> findByIdAndDeletedFalse(Long id);

    List<Season> findAllByLeagueIdAndDeletedFalse(Long leagueId);

    List<Season> findAllByStatusAndDeletedFalse(SeasonStatus status);

    boolean existsByCodeAndDeletedFalse(String code);
}
