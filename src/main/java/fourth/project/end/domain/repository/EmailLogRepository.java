package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.enums.EmailLogStatus;
import fourth.project.end.domain.model.EmailLog;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findAllByStatus(EmailLogStatus status);
}
