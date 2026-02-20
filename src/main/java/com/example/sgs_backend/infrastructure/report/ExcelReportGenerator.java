package com.example.sgs_backend.infrastructure.report;

import com.example.sgs_backend.application.report.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Générateur Excel avec Apache POI
 */
@Component
@Slf4j
public class ExcelReportGenerator {

    public byte[] generate(ReportRequest request) {
        log.info("Génération Excel pour {}", request.reportType());
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // TODO: Implémenter avec Apache POI
             Workbook workbook = new XSSFWorkbook();
             Sheet sheet = workbook.createSheet("Rapport");
             Row row = sheet.createRow(0);
             row.createCell(0).setCellValue("Rapport " + request.reportType());
             workbook.write(baos);
             workbook.close();
            
            String content = "Rapport Excel - " + request.reportType();
            return content.getBytes();
            
        } catch (Exception e) {
            log.error("Erreur génération Excel", e);
            return new byte[0];
        }
    }
}
