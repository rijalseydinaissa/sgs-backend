package com.example.sgs_backend.application.expense.port;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.expense.Expense;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends BaseRepository<Expense, UUID> {
    Optional<Expense> findByReference(String reference);
    boolean existsByReference(String reference);
}
