package com.example.sgs_backend.infrastructure.persistence.report;

import com.example.sgs_backend.domain.report.Report;
import com.example.sgs_backend.domain.report.ReportType;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ReportJpaRepository extends BaseJpaRepository<Report, UUID> {
    List<Report> findByReportTypeAndDeletedFalse(ReportType type);
    List<Report> findByGeneratedByUserIdAndDeletedFalse(UUID userId);
}
