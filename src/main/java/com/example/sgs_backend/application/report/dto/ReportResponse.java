package com.example.sgs_backend.application.report.dto;

import com.example.sgs_backend.domain.report.ReportFormat;
import com.example.sgs_backend.domain.report.ReportType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponse(
    UUID id,
    String fileName,
    ReportType reportType,
    ReportFormat format,
    String filePath,
    Long fileSize,
    LocalDateTime generatedAt,
    UUID generatedByUserId
) {}
