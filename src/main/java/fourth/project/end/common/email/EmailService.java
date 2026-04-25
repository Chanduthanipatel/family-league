package fourth.project.end.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    public EmailService(JavaMailSender mailSender, EmailProperties emailProperties) {
        this.mailSender = mailSender;
        this.emailProperties = emailProperties;
    }

    @Async
    public void send(EmailMessage message) {
        if (!emailProperties.isEnabled()) {
            log.info("[EMAIL DISABLED] To: {} | Subject: {}", message.to(), message.subject());
            return;
        }

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(emailProperties.getFrom());
            mail.setTo(message.to());
            mail.setSubject(message.subject());
            mail.setText(message.body());
            mailSender.send(mail);
            log.info("Email sent to: {}", message.to());
        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}", message.to(), ex.getMessage());
        }
    }
}
