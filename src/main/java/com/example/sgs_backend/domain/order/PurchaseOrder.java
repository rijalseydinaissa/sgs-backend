package com.example.sgs_backend.domain.order;

import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.example.sgs_backend.domain.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "purchase_orders")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class PurchaseOrder extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String reference;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money totalAmount;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PurchaseOrderLine> lines = new ArrayList<>();

    public void addLine(PurchaseOrderLine line) {
        line.setPurchaseOrder(this); this.lines.add(line);
    }

    public void send() {
        if (status != PurchaseOrderStatus.DRAFT)
            throw new BusinessRuleException("Seule une commande DRAFT peut être envoyée");
        this.status = PurchaseOrderStatus.SENT;
    }
}
