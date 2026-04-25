package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.EmailLogStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_log")
public class EmailLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_campaign_id")
    private NotificationCampaign notificationCampaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(name = "recipient_email", nullable = false, length = 255)
    private String recipientEmail;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private EmailLogStatus status = EmailLogStatus.PENDING;

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "failure_reason", columnDefinition = "text")
    private String failureReason;

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

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public EmailLogStatus getStatus() {
        return status;
    }

    public void setStatus(EmailLogStatus status) {
        this.status = status;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
