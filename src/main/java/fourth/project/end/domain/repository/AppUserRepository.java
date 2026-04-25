package fourth.project.end.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndDeletedFalse(String email);
}
