package com.example.sgs_backend.domain.notification;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ✅ extends BaseEntity
 * Historique des notifications envoyées
 */
@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Notification extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 20)
    private NotificationType notificationType;

    @Column(name = "recipient", nullable = false, length = 255)
    private String recipient;  // email ou numéro téléphone

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "user_id")
    private UUID userId;
}
