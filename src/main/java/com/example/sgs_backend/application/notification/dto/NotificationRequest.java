package com.example.sgs_backend.application.notification.dto;

import com.example.sgs_backend.domain.notification.NotificationType;

public record NotificationRequest(NotificationType type, String recipient, String subject, String content) {}
