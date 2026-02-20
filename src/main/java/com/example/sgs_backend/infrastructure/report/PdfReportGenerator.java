package com.example.sgs_backend.infrastructure.report;

import com.example.sgs_backend.application.report.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Générateur PDF avec iText 7
 */
@Component
@Slf4j
public class PdfReportGenerator {

    public byte[] generate(ReportRequest request) {
        log.info("Génération PDF pour {}", request.reportType());
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // TODO: Implémenter avec iText 7
            // Document document = new Document(new PdfDocument(new PdfWriter(baos)));
            // document.add(new Paragraph("Rapport " + request.reportType()));
            // document.close();
            
            String content = "Rapport PDF - " + request.reportType() + "\n" +
                           "Période: " + request.startDate() + " - " + request.endDate();
            return content.getBytes();
            
        } catch (Exception e) {
            log.error("Erreur génération PDF", e);
            return new byte[0];
        }
    }
}
