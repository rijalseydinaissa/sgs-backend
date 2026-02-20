package com.example.sgs_backend.domain.customer;

import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.example.sgs_backend.domain.common.valueobject.Address;
import com.example.sgs_backend.domain.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

/**
 * ✅ extends BaseEntity + Address VO + Money VO
 * Logique métier : gestion crédit client
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customers_code", columnList = "code"),
    @Index(name = "idx_customers_email", columnList = "email")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Customer extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", length = 20)
    private CustomerType customerType = CustomerType.INDIVIDUAL;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(name = "tax_number", length = 50)
    private String taxNumber;

    // ✅ Address VO @Embedded
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "city", column = @Column(name = "address_city")),
        @AttributeOverride(name = "region", column = @Column(name = "address_region")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
        @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private Address address;

    // ✅ Money VO @Embedded (solde compte client)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "account_balance")),
        @AttributeOverride(name = "currency", column = @Column(name = "balance_currency", length = 3))
    })
    private Money accountBalance;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    private java.math.BigDecimal creditLimit;

    @Column(nullable = false)
    private boolean active = true;

    // ── Logique métier crédit client ──────────────────────────────

    /** Ajouter du crédit au compte client */
    public void addCredit(Money amount) {
        if (this.accountBalance == null) {
            this.accountBalance = amount;
        } else {
            this.accountBalance = this.accountBalance.add(amount);
        }
    }

    /** Déduire un paiement du crédit */
    public void deductCredit(Money amount) {
        if (this.accountBalance == null || this.accountBalance.isLessThan(amount)) {
            throw new BusinessRuleException("Crédit insuffisant pour ce client");
        }
        this.accountBalance = this.accountBalance.subtract(amount);
    }

    public boolean hasCredit() {
        return accountBalance != null && !accountBalance.isZero();
    }

    public boolean canPurchase(Money amount) {
        if (creditLimit == null) return true;
        Money currentDebt = accountBalance != null ? accountBalance : Money.zero("XOF");
        return currentDebt.add(amount).getAmount().compareTo(creditLimit) <= 0;
    }
}
