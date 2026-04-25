package fourth.project.end.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "notification_campaign_recipient",
    uniqueConstraints = @UniqueConstraint(name = "uk_notification_campaign_recipient", columnNames = {"notification_campaign_id", "user_id"})
)
public class NotificationCampaignRecipient extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_campaign_id", nullable = false)
    private NotificationCampaign notificationCampaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public NotificationCampaign getNotificationCampaign() {
        return notificationCampaign;
    }

    public void setNotificationCampaign(NotificationCampaign notificationCampaign) {
        this.notificationCampaign = notificationCampaign;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
