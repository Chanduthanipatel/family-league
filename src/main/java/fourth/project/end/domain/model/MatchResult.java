package fourth.project.end.domain.model;

import java.time.Instant;

import fourth.project.end.domain.enums.MatchResultType;
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
@Table(name = "match_result")
public class MatchResult extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_match_id", nullable = false, unique = true)
    private LeagueMatch leagueMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winning_team_id")
    private Team winningTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toss_winner_team_id")
    private Team tossWinnerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_of_match_id")
    private Player playerOfMatch;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false, length = 30)
    private MatchResultType resultType = MatchResultType.NORMAL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "published_by", nullable = false)
    private AppUser publishedBy;

    @Column(name = "published_at", nullable = false)
    private Instant publishedAt;

    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;

    public LeagueMatch getLeagueMatch() {
        return leagueMatch;
    }

    public void setLeagueMatch(LeagueMatch leagueMatch) {
        this.leagueMatch = leagueMatch;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Team getTossWinnerTeam() {
        return tossWinnerTeam;
    }

    public void setTossWinnerTeam(Team tossWinnerTeam) {
        this.tossWinnerTeam = tossWinnerTeam;
    }

    public Player getPlayerOfMatch() {
        return playerOfMatch;
    }

    public void setPlayerOfMatch(Player playerOfMatch) {
        this.playerOfMatch = playerOfMatch;
    }

    public MatchResultType getResultType() {
        return resultType;
    }

    public void setResultType(MatchResultType resultType) {
        this.resultType = resultType;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
