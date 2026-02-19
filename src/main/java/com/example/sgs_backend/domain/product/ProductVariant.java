package com.example.sgs_backend.domain.product;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * ✅ extends BaseEntity
 * Variante d'un produit : taille, couleur, capacité, etc.
 * Ex: T-shirt → S/M/L/XL, Téléphone → 64Go/128Go/256Go
 */
@Entity
@Table(name = "product_variants", indexes = {
    @Index(name = "idx_variants_product", columnList = "product_id"),
    @Index(name = "idx_variants_sku",     columnList = "sku")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true, length = 80)
    private String sku;           // Stock Keeping Unit — ex: "SHIRT-RED-M"

    @Column(name = "attribute_name", length = 50)
    private String attributeName; // ex: "Couleur", "Taille", "Capacité"

    @Column(name = "attribute_value", length = 100)
    private String attributeValue; // ex: "Rouge", "M", "128Go"

    @Column(name = "price_adjustment", precision = 15, scale = 2)
    private BigDecimal priceAdjustment = BigDecimal.ZERO; // +/- par rapport au prix du produit

    @Column(name = "current_stock", nullable = false)
    private int currentStock = 0;

    @Column(unique = true, length = 50)
    private String barcode;

    @Column(nullable = false)
    private boolean active = true;

    public BigDecimal getEffectivePrice(BigDecimal basePrice) {
        return basePrice.add(priceAdjustment);
    }
}
