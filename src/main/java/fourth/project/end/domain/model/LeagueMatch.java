package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.MatchStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "league_match")
public class LeagueMatch extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(name = "match_number")
    private Integer matchNumber;

    @Column(name = "venue", length = 255)
    private String venue;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "prediction_lock_at", nullable = false)
    private Instant predictionLockAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private MatchStatus status = MatchStatus.SCHEDULED;

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getPredictionLockAt() {
        return predictionLockAt;
    }

    public void setPredictionLockAt(Instant predictionLockAt) {
        this.predictionLockAt = predictionLockAt;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }
}
