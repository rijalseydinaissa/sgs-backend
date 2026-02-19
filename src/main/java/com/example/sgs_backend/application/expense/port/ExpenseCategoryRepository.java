package com.example.sgs_backend.application.expense.port;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.expense.ExpenseCategory;

import java.util.*;

public interface ExpenseCategoryRepository extends BaseRepository<ExpenseCategory, UUID> {
    Optional<ExpenseCategory> findByCode(String code);
    boolean existsByCode(String code);
}
