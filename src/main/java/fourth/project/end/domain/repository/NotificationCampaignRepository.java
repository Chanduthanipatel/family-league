package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.enums.NotificationCampaignStatus;
import fourth.project.end.domain.model.NotificationCampaign;

public interface NotificationCampaignRepository extends JpaRepository<NotificationCampaign, Long> {

    List<NotificationCampaign> findAllByStatusAndDeletedFalse(NotificationCampaignStatus status);
}
