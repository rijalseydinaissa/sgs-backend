package com.example.sgs_backend.infrastructure.notification.sms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsNotificationAdapter {
    public void send(String phone, String message) {
        log.info("SMS envoyé à {} : {}", phone, message);
        // TODO: Twilio
    }
}
