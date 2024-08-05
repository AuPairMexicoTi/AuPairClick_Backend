package com.aupair.aupaircl.service.notificationservice;

import com.aupair.aupaircl.model.notfication.Notification;
import com.aupair.aupaircl.model.notfication.NotificationRepository;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getNotificationUnRead(UserEmailDto userEmailDto) {
        try {
            List<Notification> notificationList = this.notificationRepository.findAllByUser_EmailAndUser_IsLockedAndReadStatus(userEmailDto.getEmail(), false, false);

            if(notificationList.isEmpty()) {
                log.error("No hay notificaciones del usuario");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, 400, "No hay notificaciones del usuario"));
            }

            int listSize = notificationList.size();
            List<Notification> lastFourNotifications = notificationList.subList(Math.max(0, listSize - 4), listSize);

            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Notificaciones del usuario", HttpStatus.OK.value(), false, lastFourNotifications));
        } catch (Exception e) {
            log.error("Algo ocurrio al obtener las notificaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse(true, 500, "Algo sucedio al obtener las notificaciones"));
        }
    }

}
