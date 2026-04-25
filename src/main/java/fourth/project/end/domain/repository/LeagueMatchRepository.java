package fourth.project.end.domain.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.LeagueMatch;

public interface LeagueMatchRepository extends JpaRepository<LeagueMatch, Long> {

    List<LeagueMatch> findAllBySeasonIdAndDeletedFalseOrderByStartsAtAsc(Long seasonId);

    Optional<LeagueMatch> findByIdAndDeletedFalse(Long id);

    List<LeagueMatch> findAllByPredictionLockAtBeforeAndDeletedFalse(Instant cutoffTime);
}
