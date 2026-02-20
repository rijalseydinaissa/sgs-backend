package com.example.sgs_backend.infrastructure.persistence.expense;

import com.example.sgs_backend.domain.expense.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ExpenseCategoryJpaRepository
        extends JpaRepository<ExpenseCategory, UUID> {

    Optional<ExpenseCategory> findByCode(String code);
    boolean existsByCode(String code);

    Optional<ExpenseCategory> findByIdAndDeletedFalse(UUID id);
}
