package com.example.sgs_backend.api.report;


import com.example.sgs_backend.application.report.ReportGeneratorService;
import com.example.sgs_backend.application.report.dto.ReportRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.sgs_backend.domain.report.ReportFormat.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reports", description = "Génération rapports PDF/Excel/CSV")
public class ReportController {

    private final ReportGeneratorService reportService;

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('REPORT_GENERATE')")
    @Operation(summary = "Générer un rapport")
    public ResponseEntity<byte[]> generateReport(
            @RequestBody ReportRequest request,
            @RequestParam UUID userId) {

        byte[] report = reportService.generateReport(request, userId);

        String contentType = switch (request.format()) {
            case PDF -> "application/pdf";
            case EXCEL -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case CSV -> "text/csv";
            case JSON -> "application/json";
        };
        
        String extension = request.format().name().toLowerCase();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=rapport_" + request.reportType() + "." + extension)
                .body(report);
    }
}
