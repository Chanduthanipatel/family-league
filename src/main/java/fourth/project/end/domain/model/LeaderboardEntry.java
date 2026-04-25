package fourth.project.end.domain.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "leaderboard_entry",
    uniqueConstraints = @UniqueConstraint(name = "uk_leaderboard_entry", columnNames = {"season_id", "user_id"})
)
public class LeaderboardEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;

    @Column(name = "rank_position")
    private Integer rankPosition;

    @Column(name = "last_recalculated_at")
    private Instant lastRecalculatedAt;

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

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(Integer rankPosition) {
        this.rankPosition = rankPosition;
    }

    public Instant getLastRecalculatedAt() {
        return lastRecalculatedAt;
    }

    public void setLastRecalculatedAt(Instant lastRecalculatedAt) {
        this.lastRecalculatedAt = lastRecalculatedAt;
    }
}
