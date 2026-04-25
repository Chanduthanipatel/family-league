package fourth.project.end.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "season_team",
    uniqueConstraints = @UniqueConstraint(name = "uk_season_team", columnNames = {"season_id", "team_id"})
)
public class SeasonTeam extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "seeded_position")
    private Integer seededPosition;

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getSeededPosition() {
        return seededPosition;
    }

    public void setSeededPosition(Integer seededPosition) {
        this.seededPosition = seededPosition;
    }
}
