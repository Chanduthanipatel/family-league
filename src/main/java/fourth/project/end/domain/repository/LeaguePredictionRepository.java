package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.LeaguePrediction;

public interface LeaguePredictionRepository extends JpaRepository<LeaguePrediction, Long> {

    Optional<LeaguePrediction> findBySeasonIdAndUserIdAndDeletedFalse(Long seasonId, Long userId);

    List<LeaguePrediction> findAllBySeasonIdAndDeletedFalse(Long seasonId);
}
