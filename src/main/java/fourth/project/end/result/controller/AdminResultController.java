package fourth.project.end.result.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.result.dto.PublishMatchResultRequest;
import fourth.project.end.result.dto.PublishSeasonResultRequest;
import fourth.project.end.result.dto.PublishedMatchResultResponse;
import fourth.project.end.result.dto.PublishedSeasonResultResponse;
import fourth.project.end.result.service.ResultProcessingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/results")
@PreAuthorize("hasRole('ADMIN')")
public class AdminResultController {

    private final ResultProcessingService resultProcessingService;

    public AdminResultController(ResultProcessingService resultProcessingService) {
        this.resultProcessingService = resultProcessingService;
    }

    @PostMapping("/matches/{matchId}")
    public PublishedMatchResultResponse publishMatchResult(
        @PathVariable Long matchId,
        @Valid @RequestBody PublishMatchResultRequest request,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return resultProcessingService.publishMatchResult(matchId, request, principal);
    }

    @GetMapping("/matches/{matchId}")
    public PublishedMatchResultResponse getMatchResult(@PathVariable Long matchId) {
        return resultProcessingService.getMatchResult(matchId);
    }

    @PostMapping("/seasons/{seasonId}")
    public PublishedSeasonResultResponse publishSeasonResult(
        @PathVariable Long seasonId,
        @Valid @RequestBody PublishSeasonResultRequest request,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return resultProcessingService.publishSeasonResult(seasonId, request, principal);
    }

    @GetMapping("/seasons/{seasonId}")
    public PublishedSeasonResultResponse getSeasonResult(@PathVariable Long seasonId) {
        return resultProcessingService.getSeasonResult(seasonId);
    }
}
