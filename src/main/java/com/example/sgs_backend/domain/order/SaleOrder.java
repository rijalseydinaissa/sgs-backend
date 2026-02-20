package com.example.sgs_backend.domain.order;

import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "sale_orders")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class SaleOrder extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String reference;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SaleOrderStatus status = SaleOrderStatus.QUOTE;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money totalAmount;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SaleOrderLine> lines = new ArrayList<>();
}
