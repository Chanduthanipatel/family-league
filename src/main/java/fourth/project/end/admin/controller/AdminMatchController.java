package fourth.project.end.admin.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.admin.dto.CreateMatchRequest;
import fourth.project.end.admin.dto.MatchResponse;
import fourth.project.end.admin.service.AdminMatchService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMatchController {

    private final AdminMatchService adminMatchService;

    public AdminMatchController(AdminMatchService adminMatchService) {
        this.adminMatchService = adminMatchService;
    }

    @PostMapping("/seasons/{seasonId}/matches")
    public MatchResponse createMatch(
        @PathVariable Long seasonId,
        @Valid @RequestBody CreateMatchRequest request
    ) {
        return adminMatchService.createMatch(seasonId, request);
    }

    @GetMapping("/seasons/{seasonId}/matches")
    public List<MatchResponse> listMatches(@PathVariable Long seasonId) {
        return adminMatchService.listMatches(seasonId);
    }
}
