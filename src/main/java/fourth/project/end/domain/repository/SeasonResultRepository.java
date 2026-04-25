package fourth.project.end.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.SeasonResult;

public interface SeasonResultRepository extends JpaRepository<SeasonResult, Long> {

    Optional<SeasonResult> findBySeasonIdAndDeletedFalse(Long seasonId);
}
