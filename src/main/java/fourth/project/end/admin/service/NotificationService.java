package fourth.project.end.admin.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.admin.dto.SendAlertResponse;
import fourth.project.end.admin.dto.SendMatchAlertRequest;
import fourth.project.end.common.email.EmailMessage;
import fourth.project.end.common.email.EmailService;
import fourth.project.end.common.exception.ResourceNotFoundException;
import fourth.project.end.domain.model.AppUser;
import fourth.project.end.domain.model.LeagueMatch;
import fourth.project.end.domain.repository.AppUserRepository;
import fourth.project.end.domain.repository.LeagueMatchRepository;

@Service
public class NotificationService {

    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a z")
            .withZone(ZoneId.of("Asia/Kolkata"));

    private final EmailService emailService;
    private final LeagueMatchRepository leagueMatchRepository;
    private final AppUserRepository appUserRepository;

    public NotificationService(
            EmailService emailService,
            LeagueMatchRepository leagueMatchRepository,
            AppUserRepository appUserRepository) {
        this.emailService = emailService;
        this.leagueMatchRepository = leagueMatchRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public SendAlertResponse sendMatchAlert(SendMatchAlertRequest request) {
        LeagueMatch match = leagueMatchRepository.findByIdAndDeletedFalse(request.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + request.matchId()));

        String homeTeam = match.getHomeTeam().getName();
        String awayTeam = match.getAwayTeam().getName();
        String matchTime = DISPLAY_FMT.format(match.getStartsAt());
        String lockTime = DISPLAY_FMT.format(match.getPredictionLockAt());

        String subject = "🏏 Match Alert: " + homeTeam + " vs " + awayTeam + " — Make Your Predictions!";

        String body = buildMatchAlertBody(homeTeam, awayTeam, matchTime, lockTime, request.customMessage());

        List<AppUser> users = appUserRepository.findAll().stream()
                .filter(u -> !u.isDeleted())
                .toList();

        users.forEach(user -> emailService.send(new EmailMessage(user.getEmail(), subject, body)));

        return new SendAlertResponse(users.size(),
                "Match alert queued for " + users.size() + " user(s)");
    }

    @Transactional(readOnly = true)
    public SendAlertResponse sendCustomAlert(String subject, String body) {
        List<AppUser> users = appUserRepository.findAll().stream()
                .filter(u -> !u.isDeleted())
                .toList();

        users.forEach(user -> emailService.send(new EmailMessage(user.getEmail(), subject, body)));

        return new SendAlertResponse(users.size(),
                "Custom alert queued for " + users.size() + " user(s)");
    }

    private String buildMatchAlertBody(
            String homeTeam,
            String awayTeam,
            String matchTime,
            String lockTime,
            String customMessage) {
        return """
                Hi there! 👋

                %s

                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                🏏 Match: %s vs %s
                🕐 Match Time: %s
                🔒 Predictions close at: %s
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                Log in and submit your predictions before the window closes!

                Good luck! 🍀
                — Family League Team
                """.formatted(customMessage, homeTeam, awayTeam, matchTime, lockTime);
    }
}
