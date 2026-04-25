package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.NotificationCampaignStatus;
import fourth.project.end.domain.enums.NotificationCampaignType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_campaign")
public class NotificationCampaign extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private AppUser createdByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_type", nullable = false, length = 30)
    private NotificationCampaignType campaignType;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "message_body", nullable = false, columnDefinition = "text")
    private String messageBody;

    @Column(name = "scheduled_at")
    private Instant scheduledAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private NotificationCampaignStatus status = NotificationCampaignStatus.DRAFT;

    public AppUser getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(AppUser createdByUser) {
        this.createdByUser = createdByUser;
    }

    public NotificationCampaignType getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(NotificationCampaignType campaignType) {
        this.campaignType = campaignType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Instant scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public NotificationCampaignStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationCampaignStatus status) {
        this.status = status;
    }
}
