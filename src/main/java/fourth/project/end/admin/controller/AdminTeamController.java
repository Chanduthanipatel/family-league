package fourth.project.end.admin.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.admin.dto.AssignSeasonTeamRequest;
import fourth.project.end.admin.dto.CreateTeamRequest;
import fourth.project.end.admin.dto.SeasonTeamResponse;
import fourth.project.end.admin.dto.TeamResponse;
import fourth.project.end.admin.service.AdminTeamService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeamController {

    private final AdminTeamService adminTeamService;

    public AdminTeamController(AdminTeamService adminTeamService) {
        this.adminTeamService = adminTeamService;
    }

    @PostMapping("/teams")
    public TeamResponse createTeam(@Valid @RequestBody CreateTeamRequest request) {
        return adminTeamService.createTeam(request);
    }

    @GetMapping("/teams")
    public List<TeamResponse> listTeams() {
        return adminTeamService.listTeams();
    }

    @PostMapping("/seasons/{seasonId}/teams")
    public SeasonTeamResponse assignTeamToSeason(
        @PathVariable Long seasonId,
        @Valid @RequestBody AssignSeasonTeamRequest request
    ) {
        return adminTeamService.assignTeamToSeason(seasonId, request);
    }

    @GetMapping("/seasons/{seasonId}/teams")
    public List<SeasonTeamResponse> listSeasonTeams(@PathVariable Long seasonId) {
        return adminTeamService.listSeasonTeams(seasonId);
    }
}
