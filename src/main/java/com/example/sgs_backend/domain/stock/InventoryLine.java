package com.example.sgs_backend.domain.stock;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Quantity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * ✅ extends BaseEntity + utilise Quantity VO
 * Ligne d'inventaire : stock théorique vs stock compté.
 */
@Entity
@Table(name = "inventory_lines")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class InventoryLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    // ✅ Stock théorique (système) — Quantity VO
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "theoretical_qty_value")),
        @AttributeOverride(name = "unit",  column = @Column(name = "theoretical_qty_unit", length = 20))
    })
    private Quantity theoreticalQuantity;

    // ✅ Stock compté (physique) — Quantity VO
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "counted_qty_value")),
        @AttributeOverride(name = "unit",  column = @Column(name = "counted_qty_unit", length = 20))
    })
    private Quantity countedQuantity;

    @Column(length = 200)
    private String notes;

    // ── Logique métier ────────────────────────────────────────────

    /** Écart = compté - théorique (positif = excédent, négatif = manquant) */
    public Quantity getDiscrepancy() {
        return countedQuantity.differenceWith(theoreticalQuantity);
    }

    public boolean hasDiscrepancy() {
        return !getDiscrepancy().isZero();
    }
}
