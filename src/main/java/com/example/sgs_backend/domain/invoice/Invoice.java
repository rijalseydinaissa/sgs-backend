package com.example.sgs_backend.domain.invoice;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "invoices")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Invoice extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String reference;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_ttc")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money totalTTC;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "paid_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "paid_currency", length = 3))
    })
    private Money paidAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @Builder.Default
    private List<InvoiceLine> lines = new ArrayList<>();

    public void recordPayment(Money amount) {
        this.paidAmount = this.paidAmount != null ? this.paidAmount.add(amount) : amount;
        if (this.paidAmount.isGreaterThan(this.totalTTC)) {
            this.status = InvoiceStatus.PAID;
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }
    }
}
