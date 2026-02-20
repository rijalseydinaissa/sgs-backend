package com.example.sgs_backend.infrastructure.report;

import com.example.sgs_backend.application.report.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Générateur CSV
 */
@Component
@Slf4j
public class CsvReportGenerator {

    public byte[] generate(ReportRequest request) {
        log.info("Génération CSV pour {}", request.reportType());
        
        StringBuilder csv = new StringBuilder();
        csv.append("Type,Début,Fin\n");
        csv.append(request.reportType()).append(",");
        csv.append(request.startDate()).append(",");
        csv.append(request.endDate()).append("\n");
        
        return csv.toString().getBytes();
    }
}
