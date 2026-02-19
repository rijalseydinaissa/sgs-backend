package com.example.sgs_backend.domain.expense;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * ✅ extends BaseEntity + utilise Money VO
 * Budget alloué par catégorie de dépense pour une période donnée.
 */
@Entity
@Table(name = "budgets", indexes = {
    @Index(name = "idx_budgets_category", columnList = "category_id"),
    @Index(name = "idx_budgets_period", columnList = "period_start, period_end")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Budget extends BaseEntity {

    @Column(nullable = false, length = 60)
    private String name;  // "Budget Janvier 2024", "Budget Q1 2024"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    private BudgetPeriod periodType;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    // ✅ Money VO @Embedded
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount",   column = @Column(name = "allocated_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money allocatedAmount;  // Montant budgété

    @Column(name = "site_id")
    private UUID siteId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Montant dépensé (calculé dynamiquement depuis les Expenses — pas stocké)
    // public Money getSpentAmount() → dans le service

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(periodStart) && !now.isAfter(periodEnd);
    }
}
