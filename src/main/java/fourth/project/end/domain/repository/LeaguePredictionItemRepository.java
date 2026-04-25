package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.LeaguePredictionItem;

public interface LeaguePredictionItemRepository extends JpaRepository<LeaguePredictionItem, Long> {

    List<LeaguePredictionItem> findAllByLeaguePredictionIdAndDeletedFalseOrderByPredictedPositionAsc(Long leaguePredictionId);

    void deleteAllByLeaguePredictionId(Long leaguePredictionId);
}
