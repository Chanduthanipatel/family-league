package fourth.project.end.domain.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.League;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByCodeAndDeletedFalse(String code);

    Optional<League> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);

    List<League> findAllByDeletedFalseOrderByNameAsc();
}
