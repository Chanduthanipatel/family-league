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
    name = "league_prediction_item",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_league_prediction_item_position", columnNames = {"league_prediction_id", "predicted_position"}),
        @UniqueConstraint(name = "uk_league_prediction_item_team", columnNames = {"league_prediction_id", "season_team_id"})
    }
)
public class LeaguePredictionItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_prediction_id", nullable = false)
    private LeaguePrediction leaguePrediction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_team_id", nullable = false)
    private SeasonTeam seasonTeam;

    @Column(name = "predicted_position", nullable = false)
    private Integer predictedPosition;

    public LeaguePrediction getLeaguePrediction() {
        return leaguePrediction;
    }

    public void setLeaguePrediction(LeaguePrediction leaguePrediction) {
        this.leaguePrediction = leaguePrediction;
    }

    public SeasonTeam getSeasonTeam() {
        return seasonTeam;
    }

    public void setSeasonTeam(SeasonTeam seasonTeam) {
        this.seasonTeam = seasonTeam;
    }

    public Integer getPredictedPosition() {
        return predictedPosition;
    }

    public void setPredictedPosition(Integer predictedPosition) {
        this.predictedPosition = predictedPosition;
    }
}
