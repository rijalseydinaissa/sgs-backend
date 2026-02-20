package com.example.sgs_backend.application.notification;

import com.example.sgs_backend.application.notification.dto.NotificationRequest;
import com.example.sgs_backend.infrastructure.notification.email.EmailNotificationAdapter;
import com.example.sgs_backend.infrastructure.notification.sms.SmsNotificationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailNotificationAdapter emailAdapter;
    private final SmsNotificationAdapter smsAdapter;
    
    public void send(NotificationRequest request) {
        switch (request.type()) {
            case EMAIL -> emailAdapter.send(request.recipient(), request.subject(), request.content());
            case SMS -> smsAdapter.send(request.recipient(), request.content());
            default -> throw new IllegalArgumentException("Type non supporté");
        }
    }
}
