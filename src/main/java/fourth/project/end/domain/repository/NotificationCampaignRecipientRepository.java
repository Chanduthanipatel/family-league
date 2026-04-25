package fourth.project.end.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fourth.project.end.domain.model.NotificationCampaignRecipient;

public interface NotificationCampaignRecipientRepository extends JpaRepository<NotificationCampaignRecipient, Long> {

    List<NotificationCampaignRecipient> findAllByNotificationCampaignIdAndDeletedFalse(Long notificationCampaignId);
}
