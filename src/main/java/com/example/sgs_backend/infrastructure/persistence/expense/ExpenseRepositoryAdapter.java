package com.example.sgs_backend.infrastructure.persistence.expense;

import com.example.sgs_backend.application.expense.port.ExpenseRepository;
import com.example.sgs_backend.domain.expense.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository @RequiredArgsConstructor
public class ExpenseRepositoryAdapter implements ExpenseRepository {
    private final ExpenseJpaRepository jpa;

    @Override public Expense save(Expense e) { return jpa.save(e); }
    @Override public Optional<Expense> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<Expense> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Expense> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Expense> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public Optional<Expense> findByReference(String ref) { return jpa.findByReferenceAndDeletedFalse(ref); }
    @Override public boolean existsByReference(String ref) { return jpa.existsByReferenceAndDeletedFalse(ref); }
}
