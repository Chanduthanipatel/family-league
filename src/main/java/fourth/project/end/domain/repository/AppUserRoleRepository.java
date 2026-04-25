package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.AppUserRole;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {

    List<AppUserRole> findAllByUserIdAndDeletedFalse(Long userId);
}
