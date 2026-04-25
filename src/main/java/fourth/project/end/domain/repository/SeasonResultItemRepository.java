package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.SeasonResultItem;

public interface SeasonResultItemRepository extends JpaRepository<SeasonResultItem, Long> {

    List<SeasonResultItem> findAllBySeasonResultIdAndDeletedFalseOrderByFinalPositionAsc(Long seasonResultId);

    void deleteAllBySeasonResultId(Long seasonResultId);
}
