package com.example.sgs_backend.api.expense;

import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.expense.ExpenseService;
import com.example.sgs_backend.application.expense.dto.ExpenseRequest;
import com.example.sgs_backend.application.expense.dto.ExpenseResponse;
import com.example.sgs_backend.domain.expense.Expense;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/** ✅ extends BaseController<Expense, UUID, ExpenseRequest, ExpenseResponse> */
@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Expenses", description = "Dépenses + Budget + Rentabilité")
public class ExpenseController extends BaseController<Expense, UUID, ExpenseRequest, ExpenseResponse> {

    private final ExpenseService expenseService;

    @Override protected BaseService<Expense, UUID, ExpenseRequest, ExpenseResponse> getService() {
        return expenseService;
    }

    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('EXPENSE_UPDATE')")
    @Operation(summary = "Soumettre une dépense pour approbation")
    public ResponseEntity<ApiResponse<ExpenseResponse>> submit(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.submit(id, userId)));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('EXPENSE_APPROVE')")
    @Operation(summary = "Approuver une dépense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> approve(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.approve(id, userId)));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('EXPENSE_REJECT')")
    @Operation(summary = "Rejeter une dépense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> reject(
            @PathVariable UUID id, @RequestParam UUID userId, @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.reject(id, userId, reason)));
    }
}
