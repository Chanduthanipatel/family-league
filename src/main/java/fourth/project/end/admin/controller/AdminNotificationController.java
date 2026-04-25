package fourth.project.end.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.admin.dto.SendAlertResponse;
import fourth.project.end.admin.dto.SendCustomAlertRequest;
import fourth.project.end.admin.dto.SendMatchAlertRequest;
import fourth.project.end.admin.service.NotificationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {

    private final NotificationService notificationService;

    public AdminNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Send match reminder to all users — "Today's match, make predictions!"
    @PostMapping("/match-alert")
    public SendAlertResponse sendMatchAlert(@Valid @RequestBody SendMatchAlertRequest request) {
        return notificationService.sendMatchAlert(request);
    }

    // Send any custom broadcast to all users
    @PostMapping("/custom-alert")
    public SendAlertResponse sendCustomAlert(@Valid @RequestBody SendCustomAlertRequest request) {
        return notificationService.sendCustomAlert(request.subject(), request.body());
    }
}
