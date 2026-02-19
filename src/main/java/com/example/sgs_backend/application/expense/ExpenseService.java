package com.example.sgs_backend.application.expense;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.expense.dto.ExpenseRequest;
import com.example.sgs_backend.application.expense.dto.ExpenseResponse;
import com.example.sgs_backend.application.expense.port.ExpenseCategoryRepository;
import com.example.sgs_backend.application.expense.port.ExpenseRepository;
import com.example.sgs_backend.domain.common.exception.DuplicateResourceException;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.expense.Expense;
import com.example.sgs_backend.domain.expense.ExpenseCategory;
import com.example.sgs_backend.domain.expense.ExpenseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/** ✅ extends BaseService<Expense, UUID, ExpenseRequest, ExpenseResponse> */
@Service @Slf4j
public class ExpenseService extends BaseService<Expense, UUID, ExpenseRequest, ExpenseResponse> {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, 
                          ExpenseCategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override protected BaseRepository<Expense, UUID> getRepository() { return expenseRepository; }
    @Override protected String getEntityName() { return "Expense"; }

    @Override
    protected Expense toEntity(ExpenseRequest req) {
        if (expenseRepository.existsByReference(req.reference()))
            throw new DuplicateResourceException("Expense", "reference", req.reference());

        ExpenseCategory category = categoryRepository.findByIdAndDeletedFalse(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseCategory", req.categoryId()));

        Money amount = Money.of(req.amount(), req.currency() != null ? req.currency() : "XOF");

        return Expense.builder()
                .reference(req.reference()).description(req.description())
                .amount(amount).expenseDate(req.expenseDate())
                .category(category).status(ExpenseStatus.DRAFT)
                .supplierId(req.supplierId()).notes(req.notes())
                //.receiptUrl(req.receiptUrl()).siteId(req.siteId())
                .build();
    }

    @Override
    protected ExpenseResponse toResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(), e.getReference(), e.getDescription(),
                e.getAmount().getAmount(), e.getAmount().getCurrency(),
                e.getExpenseDate(),
                e.getCategory() != null ? e.getCategory().getId() : null,
                e.getCategory() != null ? e.getCategory().getName() : null,
                e.getSupplierId(), null,
                e.getApprovedByUserId(), e.getApprovedAt(), e.getRejectionReason(),
                e.getCreatedAt().toLocalDate(), e.getCreatedBy()
        );
    }

    @Override protected void updateEntity(Expense expense, ExpenseRequest req) {
        throw new UnsupportedOperationException("Dépenses ne peuvent être modifiées après création");
    }

    @Transactional
    @PreAuthorize("hasAuthority('EXPENSE_UPDATE')")
    public ExpenseResponse submit(UUID id, UUID userId) {
        Expense expense = expenseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        expense.submit(userId);
        return toResponse(expenseRepository.save(expense));
    }

    @Transactional
    @PreAuthorize("hasAuthority('EXPENSE_APPROVE')")
    public ExpenseResponse approve(UUID id, UUID userId) {
        Expense expense = expenseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        expense.approve(userId);
        return toResponse(expenseRepository.save(expense));
    }

    @Transactional
    @PreAuthorize("hasAuthority('EXPENSE_REJECT')")
    public ExpenseResponse reject(UUID id, UUID userId, String reason) {
        Expense expense = expenseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        expense.reject(userId, reason);
        return toResponse(expenseRepository.save(expense));
    }
}
