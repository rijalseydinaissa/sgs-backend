package com.example.sgs_backend.domain.product;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.example.sgs_backend.domain.common.exception.InsufficientStockException;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.common.valueobject.Quantity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ✅ CORRIGÉ — Utilise Money VO et Quantity VO (Sprint 1)
 * 
 * CHANGEMENTS vs version initiale :
 * - purchasePrice/sellingPrice → Money VO (@Embedded)
 * - currentStock/minimumStock → Quantity VO (@Embedded)
 * - addStock/removeStock → acceptent Quantity au lieu de int
 * - stockUnit supprimé → remplacé par Quantity.unit
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_products_reference", columnList = "reference"),
    @Index(name = "idx_products_barcode",   columnList = "barcode"),
    @Index(name = "idx_products_category",  columnList = "category_id"),
    @Index(name = "idx_products_status",    columnList = "status")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Product extends BaseEntity {

    // ── Identification ────────────────────────────────────────────
    @Column(nullable = false, unique = true, length = 60)
    private String reference;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, length = 50)
    private String barcode;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    // ── Catégorie ─────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    // ── Prix — Value Object Money ✅ ──────────────────────────────
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount",   column = @Column(name = "purchase_price")),
        @AttributeOverride(name = "currency", column = @Column(name = "purchase_currency", length = 3))
    })
    private Money purchasePrice;  // Prix d'achat (HT)

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount",   column = @Column(name = "selling_price", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "selling_currency", length = 3, nullable = false))
    })
    private Money sellingPrice;   // Prix de vente (HT)

    // ── Stock — Value Object Quantity ✅ ──────────────────────────
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "current_stock_value", nullable = false)),
        @AttributeOverride(name = "unit",  column = @Column(name = "current_stock_unit",  nullable = false, length = 20))
    })
    private Quantity currentStock;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "minimum_stock_value", nullable = false)),
        @AttributeOverride(name = "unit",  column = @Column(name = "minimum_stock_unit",  nullable = false, length = 20))
    })
    private Quantity minimumStock;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "maximum_stock_value")),
        @AttributeOverride(name = "unit",  column = @Column(name = "maximum_stock_unit", length = 20))
    })
    private Quantity maximumStock;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "reorder_point_value")),
        @AttributeOverride(name = "unit",  column = @Column(name = "reorder_point_unit", length = 20))
    })
    private Quantity reorderPoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_method", length = 20)
    private StockEvaluationMethod evaluationMethod = StockEvaluationMethod.WEIGHTED_AVERAGE;

    // ── Gestion expiration ────────────────────────────────────────
    @Column(name = "has_expiry_date", nullable = false)
    private boolean hasExpiryDate = false;

    @Column(name = "expiry_alert_days")
    private Integer expiryAlertDays = 30;

    // ── Statut ────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    // ── Fournisseur principal ─────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_supplier_id")
    private Supplier mainSupplier;

    @Column(name = "site_id")
    private UUID siteId;

    // ── Variantes ─────────────────────────────────────────────────
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>();

    // ══════════════════════════════════════════════════════════════
    // LOGIQUE MÉTIER — Utilise Quantity VO ✅
    // ══════════════════════════════════════════════════════════════

    /**
     * Ajouter du stock (entrée marchandise).
     * Utilise le VO Quantity pour type safety et validation automatique.
     */
    public void addStock(Quantity quantity) {
        if (!quantity.getUnit().equals(this.currentStock.getUnit())) {
            throw new BusinessRuleException(
                "Unité incompatible : " + quantity.getUnit() + 
                " vs " + this.currentStock.getUnit());
        }
        this.currentStock = this.currentStock.add(quantity);
        refreshStatus();
    }

    /**
     * Retirer du stock (sortie vente, transfert, perte).
     * Lève InsufficientStockException si stock insuffisant.
     */
    public void removeStock(Quantity quantity) {
        if (!quantity.getUnit().equals(this.currentStock.getUnit())) {
            throw new BusinessRuleException(
                "Unité incompatible : " + quantity.getUnit() + 
                " vs " + this.currentStock.getUnit());
        }
        if (!this.currentStock.canFulfill(quantity)) {
            throw new InsufficientStockException(
                name, 
                this.currentStock.getValue().intValue(), 
                quantity.getValue().intValue()
            );
        }
        this.currentStock = this.currentStock.subtract(quantity);
        refreshStatus();
    }

    /**
     * Ajustement inventaire — fixe le stock à une valeur précise.
     */
    public void adjustStock(Quantity newQuantity) {
        if (!newQuantity.getUnit().equals(this.currentStock.getUnit())) {
            throw new BusinessRuleException("Unité incompatible lors de l'ajustement");
        }
        this.currentStock = newQuantity;
        refreshStatus();
    }

    /** Vérifie si le stock est sous le seuil d'alerte */
    public boolean isLowStock() {
        return !this.currentStock.isGreaterThan(this.minimumStock);
    }

    /** Vérifie si le stock est épuisé */
    public boolean isOutOfStock() {
        return this.currentStock.isZero();
    }

    /** Retourne la valeur du stock au prix d'achat */
    public Money getStockValue() {
        if (purchasePrice == null) return Money.zero(sellingPrice.getCurrency());
        return purchasePrice.multiply(this.currentStock.getValue());
    }

    /** Retourne la marge brute (vente - achat) */
    public Money getGrossMargin() {
        if (purchasePrice == null) return Money.zero(sellingPrice.getCurrency());
        return sellingPrice.subtract(purchasePrice);
    }

    /** Pourcentage de marge */
    public double getMarginPercent() {
        if (purchasePrice == null || purchasePrice.isZero()) return 0;
        return getGrossMargin()
                .getAmount()
                .divide(purchasePrice.getAmount(), 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    /** Met à jour le statut automatiquement selon le stock */
    private void refreshStatus() {
        if (this.status == ProductStatus.ARCHIVED) return;
        if (currentStock.isZero()) {
            this.status = ProductStatus.OUT_OF_STOCK;
        } else if (this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.ACTIVE;
        }
    }

    public boolean isActive()   { return status == ProductStatus.ACTIVE; }
    public boolean isSellable() { return status == ProductStatus.ACTIVE && !currentStock.isZero(); }

    public void archive() {
        if (!currentStock.isZero())
            throw new BusinessRuleException(
                "Impossible d'archiver un produit avec du stock restant (" + 
                currentStock.getValue() + " " + currentStock.getUnit() + ")");
        this.status = ProductStatus.ARCHIVED;
    }

    public void addVariant(ProductVariant variant) {
        variant.setProduct(this);
        this.variants.add(variant);
    }
}
