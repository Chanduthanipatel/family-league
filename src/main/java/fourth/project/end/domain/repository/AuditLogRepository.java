package fourth.project.end.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
