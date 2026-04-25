package fourth.project.end.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

    Optional<AppRole> findByCodeAndDeletedFalse(String code);
}
