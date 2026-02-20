package com.example.sgs_backend.domain.report;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ✅ extends BaseEntity
 * Historique des rapports générés
 */
@Entity
@Table(name = "reports", indexes = {
    @Index(name = "idx_reports_type", columnList = "report_type"),
    @Index(name = "idx_reports_generated_at", columnList = "generated_at")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Report extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 30)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_format", nullable = false, length = 20)
    private ReportFormat format;

    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "generated_by_user_id")
    private UUID generatedByUserId;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;
}
