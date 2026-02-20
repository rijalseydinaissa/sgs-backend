package com.example.sgs_backend.infrastructure.notification.email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationAdapter {
    public void send(String to, String subject, String content) {
        log.info("Email envoyé à {} : {}", to, subject);
        // TODO: JavaMailSender
    }
}
