package com.example.sgs_backend.domain.invoice;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.expense.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Payment extends BaseEntity {

    @Column(name = "invoice_id", nullable = false)
    private UUID invoiceId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
}
