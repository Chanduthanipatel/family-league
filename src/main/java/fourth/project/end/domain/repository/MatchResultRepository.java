package fourth.project.end.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.MatchResult;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    Optional<MatchResult> findByLeagueMatchIdAndDeletedFalse(Long leagueMatchId);
}
