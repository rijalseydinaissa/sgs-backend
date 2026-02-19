package com.example.sgs_backend.domain.expense;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.example.sgs_backend.domain.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * ✅ extends BaseEntity + utilise Money VO
 * Dépense avec workflow : DRAFT → SUBMITTED → APPROVED → PAID
 */
@Entity
@Table(name = "expenses", indexes = {
    @Index(name = "idx_expenses_reference", columnList = "reference"),
    @Index(name = "idx_expenses_category", columnList = "category_id"),
    @Index(name = "idx_expenses_status", columnList = "status"),
    @Index(name = "idx_expenses_date", columnList = "expense_date")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Expense extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String reference;  // EXP-2024-001

    @Column(nullable = false, length = 200)
    private String description;

    // ✅ Money VO @Embedded
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount",   column = @Column(name = "amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExpenseStatus status = ExpenseStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "supplier_id")
    private UUID supplierId;  // Optionnel — fournisseur payé

    @Column(name = "submitted_by_user_id")
    private UUID submittedByUserId;

    @Column(name = "approved_by_user_id")
    private UUID approvedByUserId;

    @Column(name = "approved_at")
    private LocalDate approvedAt;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "receipt_url", length = 255)
    private String receiptUrl;  // URL du justificatif

    @Column(name = "site_id")
    private UUID siteId;

    // ── Logique métier ────────────────────────────────────────────

    public void submit(UUID userId) {
        if (status != ExpenseStatus.DRAFT)
            throw new BusinessRuleException("Seule une dépense DRAFT peut être soumise");
        this.status = ExpenseStatus.SUBMITTED;
        this.submittedByUserId = userId;
    }

    public void approve(UUID userId) {
        if (status != ExpenseStatus.SUBMITTED)
            throw new BusinessRuleException("Seule une dépense SUBMITTED peut être approuvée");
        this.status = ExpenseStatus.APPROVED;
        this.approvedByUserId = userId;
        this.approvedAt = LocalDate.now();
    }

    public void reject(UUID userId, String reason) {
        if (status != ExpenseStatus.SUBMITTED)
            throw new BusinessRuleException("Seule une dépense SUBMITTED peut être rejetée");
        this.status = ExpenseStatus.REJECTED;
        this.approvedByUserId = userId;
        this.rejectionReason = reason;
    }

    public void markAsPaid(PaymentMethod method, LocalDate date) {
        if (status != ExpenseStatus.APPROVED)
            throw new BusinessRuleException("Seule une dépense APPROVED peut être marquée comme payée");
        this.status = ExpenseStatus.PAID;
        this.paymentMethod = method;
        this.paymentDate = date;
    }

    public boolean isPending() { return status == ExpenseStatus.SUBMITTED; }
    public boolean isApproved() { return status == ExpenseStatus.APPROVED; }
    public boolean isPaid() { return status == ExpenseStatus.PAID; }
}
