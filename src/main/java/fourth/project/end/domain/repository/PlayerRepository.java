package fourth.project.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByCodeAndDeletedFalse(String code);

    Optional<Player> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);

    List<Player> findAllByDeletedFalseOrderByFullNameAsc();

    List<Player> findAllByActiveAndDeletedFalseOrderByFullNameAsc(boolean active);
}
