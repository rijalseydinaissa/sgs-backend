package com.example.sgs_backend.domain.order;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.common.valueobject.Quantity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "sale_order_lines")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class SaleOrderLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id", nullable = false)
    private SaleOrder saleOrder;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "quantity_value")),
        @AttributeOverride(name = "unit", column = @Column(name = "quantity_unit", length = 20))
    })
    private Quantity quantity;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "unit_price")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money unitPrice;

    public Money getTotalLine() { return unitPrice.multiply(quantity.getValue()); }
}
