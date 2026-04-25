package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.SeasonResultStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "season_result")
public class SeasonResult extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false, unique = true)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "published_by", nullable = false)
    private AppUser publishedBy;

    @Column(name = "published_at", nullable = false)
    private Instant publishedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SeasonResultStatus status = SeasonResultStatus.PUBLISHED;

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public AppUser getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(AppUser publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public SeasonResultStatus getStatus() {
        return status;
    }

    public void setStatus(SeasonResultStatus status) {
        this.status = status;
    }
}
