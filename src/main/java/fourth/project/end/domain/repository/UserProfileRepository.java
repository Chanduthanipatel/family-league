package fourth.project.end.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserIdAndDeletedFalse(Long userId);
}
