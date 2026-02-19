package com.example.sgs_backend.infrastructure.persistence.expense;

import com.example.sgs_backend.domain.expense.Expense;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ExpenseJpaRepository extends BaseJpaRepository<Expense, UUID> {
    Optional<Expense> findByReferenceAndDeletedFalse(String reference);
    boolean existsByReferenceAndDeletedFalse(String reference);
}
