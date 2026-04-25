package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.SeasonStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "season")
public class Season extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "season_year", nullable = false)
    private Integer seasonYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SeasonStatus status = SeasonStatus.DRAFT;

    @Column(name = "starts_at")
    private Instant startsAt;

    @Column(name = "ends_at")
    private Instant endsAt;

    @Column(name = "first_match_at")
    private Instant firstMatchAt;

    @Column(name = "league_prediction_lock_at")
    private Instant leaguePredictionLockAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public SeasonStatus getStatus() {
        return status;
    }

    public void setStatus(SeasonStatus status) {
        this.status = status;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public Instant getFirstMatchAt() {
        return firstMatchAt;
    }

    public void setFirstMatchAt(Instant firstMatchAt) {
        this.firstMatchAt = firstMatchAt;
    }

    public Instant getLeaguePredictionLockAt() {
        return leaguePredictionLockAt;
    }

    public void setLeaguePredictionLockAt(Instant leaguePredictionLockAt) {
        this.leaguePredictionLockAt = leaguePredictionLockAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }
}
