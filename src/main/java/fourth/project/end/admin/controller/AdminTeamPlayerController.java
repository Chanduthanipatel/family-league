package fourth.project.end.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.admin.dto.AssignPlayerToTeamRequest;
import fourth.project.end.admin.dto.TeamPlayerResponse;
import fourth.project.end.admin.service.AdminTeamPlayerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeamPlayerController {

    private final AdminTeamPlayerService adminTeamPlayerService;

    public AdminTeamPlayerController(AdminTeamPlayerService adminTeamPlayerService) {
        this.adminTeamPlayerService = adminTeamPlayerService;
    }

    // Assign a player to a season-team
    @PostMapping("/season-teams/{seasonTeamId}/players")
    public TeamPlayerResponse assignPlayer(
            @PathVariable Long seasonTeamId,
            @Valid @RequestBody AssignPlayerToTeamRequest request) {
        return adminTeamPlayerService.assignPlayerToSeasonTeam(seasonTeamId, request);
    }

    // List all players in a season-team
    @GetMapping("/season-teams/{seasonTeamId}/players")
    public List<TeamPlayerResponse> listPlayers(@PathVariable Long seasonTeamId) {
        return adminTeamPlayerService.listPlayersInSeasonTeam(seasonTeamId);
    }

    // Remove a player from a season-team
    @DeleteMapping("/season-teams/{seasonTeamId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlayer(
            @PathVariable Long seasonTeamId,
            @PathVariable Long playerId) {
        adminTeamPlayerService.removePlayerFromSeasonTeam(seasonTeamId, playerId);
    }
}
