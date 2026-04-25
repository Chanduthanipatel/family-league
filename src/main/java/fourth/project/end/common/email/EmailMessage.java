package fourth.project.end.common.email;

public record EmailMessage(
        String to,
        String subject,
        String body) {
}
