package fourth.project.end.domain.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByCodeAndDeletedFalse(String code);

    Optional<Team> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);

    List<Team> findAllByDeletedFalseOrderByNameAsc();
}
