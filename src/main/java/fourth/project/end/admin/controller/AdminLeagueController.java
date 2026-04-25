package fourth.project.end.admin.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.admin.dto.CreateLeagueRequest;
import fourth.project.end.admin.dto.CreateSeasonRequest;
import fourth.project.end.admin.dto.LeagueResponse;
import fourth.project.end.admin.dto.SeasonResponse;
import fourth.project.end.admin.dto.UpdateSeasonStatusRequest;
import fourth.project.end.admin.service.AdminLeagueService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLeagueController {

    private final AdminLeagueService adminLeagueService;

    public AdminLeagueController(AdminLeagueService adminLeagueService) {
        this.adminLeagueService = adminLeagueService;
    }

    @PostMapping("/leagues")
    public LeagueResponse createLeague(@Valid @RequestBody CreateLeagueRequest request) {
        return adminLeagueService.createLeague(request);
    }

    @GetMapping("/leagues")
    public List<LeagueResponse> listLeagues() {
        return adminLeagueService.listLeagues();
    }

    @PostMapping("/leagues/{leagueId}/seasons")
    public SeasonResponse createSeason(
        @PathVariable Long leagueId,
        @Valid @RequestBody CreateSeasonRequest request
    ) {
        return adminLeagueService.createSeason(leagueId, request);
    }

    @GetMapping("/leagues/{leagueId}/seasons")
    public List<SeasonResponse> listSeasons(@PathVariable Long leagueId) {
        return adminLeagueService.listSeasons(leagueId);
    }

    @PatchMapping("/seasons/{seasonId}/status")
    public SeasonResponse updateSeasonStatus(
        @PathVariable Long seasonId,
        @Valid @RequestBody UpdateSeasonStatusRequest request
    ) {
        return adminLeagueService.updateSeasonStatus(seasonId, request);
    }
}
