package fourth.project.end.admin.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.admin.dto.CreatePlayerRequest;
import fourth.project.end.admin.dto.PlayerResponse;
import fourth.project.end.admin.dto.UpdatePlayerRequest;
import fourth.project.end.admin.service.AdminPlayerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPlayerController {

    private final AdminPlayerService adminPlayerService;

    public AdminPlayerController(AdminPlayerService adminPlayerService) {
        this.adminPlayerService = adminPlayerService;
    }

    @PostMapping("/players")
    public PlayerResponse createPlayer(@Valid @RequestBody CreatePlayerRequest request) {
        return adminPlayerService.createPlayer(request);
    }

    @GetMapping("/players")
    public List<PlayerResponse> listPlayers(
            @RequestParam(required = false) Boolean active) {
        return adminPlayerService.listPlayers(active);
    }

    @GetMapping("/players/{playerId}")
    public PlayerResponse getPlayer(@PathVariable Long playerId) {
        return adminPlayerService.getPlayer(playerId);
    }

    @PutMapping("/players/{playerId}")
    public PlayerResponse updatePlayer(
            @PathVariable Long playerId,
            @Valid @RequestBody UpdatePlayerRequest request) {
        return adminPlayerService.updatePlayer(playerId, request);
    }
}
