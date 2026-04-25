package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.domain.model.LeaderboardEntry;

public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Long> {

    Optional<LeaderboardEntry> findBySeasonIdAndUserIdAndDeletedFalse(Long seasonId, Long userId);

    List<LeaderboardEntry> findAllBySeasonIdAndDeletedFalseOrderByRankPositionAsc(Long seasonId);

    @Transactional
    void deleteAllBySeasonId(Long seasonId);
}
