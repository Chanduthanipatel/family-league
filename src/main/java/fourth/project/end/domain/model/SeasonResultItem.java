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
    name = "season_result_item",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_season_result_position", columnNames = {"season_result_id", "final_position"}),
        @UniqueConstraint(name = "uk_season_result_team", columnNames = {"season_result_id", "season_team_id"})
    }
)
public class SeasonResultItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_result_id", nullable = false)
    private SeasonResult seasonResult;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_team_id", nullable = false)
    private SeasonTeam seasonTeam;

    @Column(name = "final_position", nullable = false)
    private Integer finalPosition;

    @Column(name = "points")
    private Integer points;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "losses")
    private Integer losses;

    @Column(name = "ties")
    private Integer ties;

    public SeasonResult getSeasonResult() {
        return seasonResult;
    }

    public void setSeasonResult(SeasonResult seasonResult) {
        this.seasonResult = seasonResult;
    }

    public SeasonTeam getSeasonTeam() {
        return seasonTeam;
    }

    public void setSeasonTeam(SeasonTeam seasonTeam) {
        this.seasonTeam = seasonTeam;
    }

    public Integer getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Integer finalPosition) {
        this.finalPosition = finalPosition;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getTies() {
        return ties;
    }

    public void setTies(Integer ties) {
        this.ties = ties;
    }
}
