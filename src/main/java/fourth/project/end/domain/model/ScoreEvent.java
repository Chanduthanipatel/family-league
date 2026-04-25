package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.ScoreEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "score_event")
public class ScoreEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_match_id")
    private LeagueMatch leagueMatch;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private ScoreEventType eventType;

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded;

    @Column(name = "source_ref", length = 100)
    private String sourceRef;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

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

    public LeagueMatch getLeagueMatch() {
        return leagueMatch;
    }

    public void setLeagueMatch(LeagueMatch leagueMatch) {
        this.leagueMatch = leagueMatch;
    }

    public ScoreEventType getEventType() {
        return eventType;
    }

    public void setEventType(ScoreEventType eventType) {
        this.eventType = eventType;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }
}
