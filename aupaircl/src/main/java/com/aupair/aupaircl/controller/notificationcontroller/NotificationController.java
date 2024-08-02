package com.aupair.aupaircl.controller.notificationcontroller;

import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.notificationservice.NotificationService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class NotificationController {

    private final NotificationService notificationService;
    @Autowired
    public NotificationController (NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @PostMapping(value = "/unread")
    public ResponseEntity<CustomResponse> getUnreadNotifications(@RequestBody UserEmailDto userEmailDto) {
        return notificationService.getNotificationUnRead(userEmailDto);

    }
}
