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
    name = "team_player",
    uniqueConstraints = @UniqueConstraint(name = "uk_team_player", columnNames = {"season_team_id", "player_id"})
)
public class TeamPlayer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_team_id", nullable = false)
    private SeasonTeam seasonTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "joined_at")
    private Instant joinedAt;

    @Column(name = "left_at")
    private Instant leftAt;

    public SeasonTeam getSeasonTeam() {
        return seasonTeam;
    }

    public void setSeasonTeam(SeasonTeam seasonTeam) {
        this.seasonTeam = seasonTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Instant getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(Instant leftAt) {
        this.leftAt = leftAt;
    }
}
