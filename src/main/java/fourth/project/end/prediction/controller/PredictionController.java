package fourth.project.end.prediction.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.prediction.dto.LeaguePredictionResponse;
import fourth.project.end.prediction.dto.MatchPredictionResponse;
import fourth.project.end.prediction.dto.SaveLeaguePredictionRequest;
import fourth.project.end.prediction.dto.SaveMatchPredictionRequest;
import fourth.project.end.prediction.service.PredictionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/predictions")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/seasons/{seasonId}")
    public LeaguePredictionResponse saveLeaguePrediction(
        @PathVariable Long seasonId,
        @Valid @RequestBody SaveLeaguePredictionRequest request,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return predictionService.saveLeaguePrediction(seasonId, request, principal);
    }

    @GetMapping("/seasons/{seasonId}/me")
    public LeaguePredictionResponse getOwnLeaguePrediction(
        @PathVariable Long seasonId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return predictionService.getOwnLeaguePrediction(seasonId, principal);
    }

    @GetMapping("/seasons/{seasonId}")
    public List<LeaguePredictionResponse> listSeasonPredictions(@PathVariable Long seasonId) {
        return predictionService.listLeaguePredictionsAfterLock(seasonId);
    }

    @PostMapping("/matches/{matchId}")
    public MatchPredictionResponse saveMatchPrediction(
        @PathVariable Long matchId,
        @Valid @RequestBody SaveMatchPredictionRequest request,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return predictionService.saveMatchPrediction(matchId, request, principal);
    }

    @GetMapping("/matches/{matchId}/me")
    public MatchPredictionResponse getOwnMatchPrediction(
        @PathVariable Long matchId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return predictionService.getOwnMatchPrediction(matchId, principal);
    }

    @GetMapping("/matches/{matchId}")
    public List<MatchPredictionResponse> listMatchPredictions(@PathVariable Long matchId) {
        return predictionService.listMatchPredictionsAfterLock(matchId);
    }
}
