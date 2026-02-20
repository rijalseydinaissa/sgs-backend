package com.example.sgs_backend.application.report;


import com.example.sgs_backend.application.report.dto.ReportRequest;
import com.example.sgs_backend.application.report.port.ReportRepository;
import com.example.sgs_backend.domain.report.Report;
import com.example.sgs_backend.infrastructure.report.CsvReportGenerator;
import com.example.sgs_backend.infrastructure.report.ExcelReportGenerator;
import com.example.sgs_backend.infrastructure.report.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service de génération de rapports
 * Délègue au bon générateur selon le format
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportGeneratorService {

    private final PdfReportGenerator pdfGenerator;
    private final ExcelReportGenerator excelGenerator;
    private final CsvReportGenerator csvGenerator;
    private final ReportRepository reportRepository;

    @Transactional
    public byte[] generateReport(ReportRequest request, UUID userId) {
        log.info("Génération rapport {} format {}", request.reportType(), request.format());

        byte[] reportBytes = switch (request.format()) {
            case PDF -> pdfGenerator.generate(request);
            case EXCEL -> excelGenerator.generate(request);
            case CSV -> csvGenerator.generate(request);
            case JSON -> generateJson(request);
        };

        // Sauvegarder l'historique
        String fileName = generateFileName(request);
        String filePath = saveToFile(fileName, reportBytes);
        
        Report report = Report.builder()
                .fileName(fileName)
                .reportType(request.reportType())
                .format(request.format())
                .filePath(filePath)
                .fileSize((long) reportBytes.length)
                .generatedByUserId(userId)
                .generatedAt(LocalDateTime.now())
                .parameters(buildParameters(request))
                .build();
        
        reportRepository.save(report);
        
        return reportBytes;
    }

    private byte[] generateJson(ReportRequest request) {
        return "{}".getBytes();
    }
    
    private String generateFileName(ReportRequest request) {
        return String.format("%s_%s_%d.%s", 
            request.reportType(), 
            request.startDate(), 
            System.currentTimeMillis(),
            request.format().name().toLowerCase()
        );
    }
    
    private String saveToFile(String fileName, byte[] content) {
        try {
            Path path = Paths.get("/var/sgs/reports", fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, content);
            return path.toString();
        } catch (Exception e) {
            log.error("Erreur sauvegarde rapport", e);
            return "/tmp/" + fileName;
        }
    }
    
    private String buildParameters(ReportRequest request) {
        return String.format("{\"startDate\":\"%s\",\"endDate\":\"%s\"}", 
            request.startDate(), request.endDate());
    }
}
