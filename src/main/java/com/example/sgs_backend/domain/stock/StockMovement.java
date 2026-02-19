package com.example.sgs_backend.domain.stock;

import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ✅ extends BaseEntity + utilise Quantity VO
 * Enregistre TOUS les mouvements de stock (entrées, sorties, transferts, pertes, ajustements).
 */
@Entity
@Table(name = "stock_movements", indexes = {
    @Index(name = "idx_movements_product",  columnList = "product_id"),
    @Index(name = "idx_movements_date",     columnList = "movement_date"),
    @Index(name = "idx_movements_type",     columnList = "movement_type"),
    @Index(name = "idx_movements_user",     columnList = "user_id")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class StockMovement extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    // ✅ Quantity VO @Embedded — supporte décimales (2.5 kg, 1.8 L...)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "quantity_value", nullable = false)),
        @AttributeOverride(name = "unit",  column = @Column(name = "quantity_unit",  nullable = false, length = 20))
    })
    private Quantity quantity;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    @Column(name = "reference", length = 80)
    private String reference;  // Référence externe (bon de livraison, facture...)

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Mouvement entre sites
    @Column(name = "from_site_id")
    private UUID fromSiteId;

    @Column(name = "to_site_id")
    private UUID toSiteId;

    // Qui a fait le mouvement
    @Column(name = "user_id")
    private UUID userId;

    // Stock avant/après (pour audit)
    @Column(name = "stock_before", precision = 15, scale = 3)
    private java.math.BigDecimal stockBefore;

    @Column(name = "stock_after", precision = 15, scale = 3)
    private java.math.BigDecimal stockAfter;

    // ── Logique métier ────────────────────────────────────────────

    public boolean isEntry()    { return movementType == MovementType.ENTRY; }
    public boolean isExit()     { return movementType == MovementType.EXIT; }
    public boolean isTransfer() { return movementType == MovementType.TRANSFER; }
    public boolean isLoss()     { return movementType == MovementType.LOSS; }
}
