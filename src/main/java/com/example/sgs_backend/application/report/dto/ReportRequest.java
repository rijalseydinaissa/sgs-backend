package com.example.sgs_backend.application.report.dto;

import com.example.sgs_backend.domain.report.ReportFormat;
import com.example.sgs_backend.domain.report.ReportType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record ReportRequest(
    @NotNull ReportType reportType,
    @NotNull ReportFormat format,
    LocalDate startDate,
    LocalDate endDate,
    UUID siteId,
    UUID customerId,
    UUID categoryId
) {}
