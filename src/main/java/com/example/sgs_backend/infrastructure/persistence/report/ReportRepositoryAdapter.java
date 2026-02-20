package com.example.sgs_backend.infrastructure.persistence.report;

import com.example.sgs_backend.application.report.port.ReportRepository;
import com.example.sgs_backend.domain.report.Report;
import com.example.sgs_backend.domain.report.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository @RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepository {
    private final ReportJpaRepository jpa;
    @Override public Report save(Report r) { return jpa.save(r); }
    @Override public Optional<Report> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<Report> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Report> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Report> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public List<Report> findByReportType(ReportType type) { return jpa.findByReportTypeAndDeletedFalse(type); }
    @Override public List<Report> findByGeneratedByUserId(UUID userId) { return jpa.findByGeneratedByUserIdAndDeletedFalse(userId); }
}
