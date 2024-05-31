package com.aupair.aupaircl.service.notificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendApprovalNotification(String email, String sectionName, boolean isApproved) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Actualización de la Aprobación del Perfil");
        if (isApproved) {
            message.setText("La sección " + sectionName + " de tu perfil ha sido aprobada.");
        } else {
            message.setText("La sección " + sectionName + " de tu perfil necesita modificaciones. Por favor, revisa y actualiza tu perfil.");
        }
        mailSender.send(message);
    }
}
