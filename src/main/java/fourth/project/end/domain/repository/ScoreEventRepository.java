package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.domain.model.ScoreEvent;

public interface ScoreEventRepository extends JpaRepository<ScoreEvent, Long> {

    List<ScoreEvent> findAllBySeasonIdAndUserIdAndDeletedFalse(Long seasonId, Long userId);

    List<ScoreEvent> findAllBySeasonIdAndDeletedFalse(Long seasonId);

    List<ScoreEvent> findAllByLeagueMatchIdAndDeletedFalse(Long leagueMatchId);

    @Transactional
    void deleteAllByLeagueMatchId(Long leagueMatchId);
}
