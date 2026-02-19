package com.example.sgs_backend.domain.product;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * ✅ extends BaseEntity
 * Relation enrichie Produit ↔ Fournisseur.
 * Un produit peut avoir plusieurs fournisseurs avec des prix différents.
 */
@Entity
@Table(name = "product_suppliers", indexes = {
    @Index(name = "idx_ps_product",  columnList = "product_id"),
    @Index(name = "idx_ps_supplier", columnList = "supplier_id")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class ProductSupplier extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "supplier_reference", length = 80)
    private String supplierReference;   // Référence chez le fournisseur

    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;        // Prix d'achat chez ce fournisseur

    @Column(name = "min_order_qty")
    private Integer minOrderQty = 1;    // Quantité minimum de commande

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;       // Délai de livraison

    @Column(name = "is_preferred", nullable = false)
    private boolean preferred = false;  // Fournisseur préféré pour ce produit

    public void markAsPreferred() { this.preferred = true; }
}
