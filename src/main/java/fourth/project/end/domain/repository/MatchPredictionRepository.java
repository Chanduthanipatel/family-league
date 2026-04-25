package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.MatchPrediction;

public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {

    Optional<MatchPrediction> findByLeagueMatchIdAndUserIdAndDeletedFalse(Long leagueMatchId, Long userId);

    List<MatchPrediction> findAllByLeagueMatchIdAndDeletedFalse(Long leagueMatchId);
}
