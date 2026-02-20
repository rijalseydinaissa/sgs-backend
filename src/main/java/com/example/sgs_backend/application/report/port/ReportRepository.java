package com.example.sgs_backend.application.report.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.report.Report;
import com.example.sgs_backend.domain.report.ReportType;

import java.util.*;

public interface ReportRepository extends BaseRepository<Report, UUID> {
    List<Report> findByReportType(ReportType type);
    List<Report> findByGeneratedByUserId(UUID userId);
}
