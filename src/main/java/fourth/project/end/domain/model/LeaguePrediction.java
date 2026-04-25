package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.PredictionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "league_prediction",
    uniqueConstraints = @UniqueConstraint(name = "uk_league_prediction", columnNames = {"season_id", "user_id"})
)
public class LeaguePrediction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "locked_at_snapshot", nullable = false)
    private Instant lockedAtSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PredictionStatus status = PredictionStatus.SUBMITTED;

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getLockedAtSnapshot() {
        return lockedAtSnapshot;
    }

    public void setLockedAtSnapshot(Instant lockedAtSnapshot) {
        this.lockedAtSnapshot = lockedAtSnapshot;
    }

    public PredictionStatus getStatus() {
        return status;
    }

    public void setStatus(PredictionStatus status) {
        this.status = status;
    }
}
