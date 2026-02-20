package com.example.sgs_backend.infrastructure.persistence.expense;

import com.example.sgs_backend.application.expense.port.ExpenseCategoryRepository;
import com.example.sgs_backend.domain.expense.ExpenseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ExpenseCategoryRepositoryAdapter
        implements ExpenseCategoryRepository {

    private final ExpenseCategoryJpaRepository jpa;

    @Override
    public ExpenseCategory save(ExpenseCategory category) {
        return jpa.save(category);
    }

    @Override
    public Optional<ExpenseCategory> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<ExpenseCategory> findByIdAndDeletedFalse(UUID id) {
        return jpa.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<ExpenseCategory> findAllByDeletedFalse() {
        return jpa.findAll().stream()
                .filter(c -> !c.isDeleted())
                .toList();
    }

    @Override
    public Page<ExpenseCategory> findAllByDeletedFalse(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ExpenseCategory> findByCode(String code) {
        return jpa.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpa.existsByCode(code);
    }

    @Override
    public void softDelete(UUID id) {
        jpa.deleteById(id); // ou soft delete custom
    }

    @Override
    public boolean existsByIdAndDeletedFalse(UUID uuid) {
        return false;
    }

    @Override
    public long countByDeletedFalse() {
        return 0;
    }
}
