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
    name = "match_prediction",
    uniqueConstraints = @UniqueConstraint(name = "uk_match_prediction", columnNames = {"league_match_id", "user_id"})
)
public class MatchPrediction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_match_id", nullable = false)
    private LeagueMatch leagueMatch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_winner_team_id")
    private Team predictedWinnerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_toss_winner_team_id")
    private Team predictedTossWinnerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_player_of_match_id")
    private Player predictedPlayerOfMatch;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "locked_at_snapshot", nullable = false)
    private Instant lockedAtSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PredictionStatus status = PredictionStatus.SUBMITTED;

    public LeagueMatch getLeagueMatch() {
        return leagueMatch;
    }

    public void setLeagueMatch(LeagueMatch leagueMatch) {
        this.leagueMatch = leagueMatch;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Team getPredictedWinnerTeam() {
        return predictedWinnerTeam;
    }

    public void setPredictedWinnerTeam(Team predictedWinnerTeam) {
        this.predictedWinnerTeam = predictedWinnerTeam;
    }

    public Team getPredictedTossWinnerTeam() {
        return predictedTossWinnerTeam;
    }

    public void setPredictedTossWinnerTeam(Team predictedTossWinnerTeam) {
        this.predictedTossWinnerTeam = predictedTossWinnerTeam;
    }

    public Player getPredictedPlayerOfMatch() {
        return predictedPlayerOfMatch;
    }

    public void setPredictedPlayerOfMatch(Player predictedPlayerOfMatch) {
        this.predictedPlayerOfMatch = predictedPlayerOfMatch;
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
