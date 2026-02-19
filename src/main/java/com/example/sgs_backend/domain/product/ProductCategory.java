package com.example.sgs_backend.domain.product;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ✅ extends BaseEntity — hérite id, audit, version, softDelete
 *
 * Catégorie arborescente (parent/enfant) :
 *   Alimentaire
 *     ├── Boissons
 *     │     ├── Eau minérale
 *     │     └── Jus de fruits
 *     └── Épicerie
 */
@Entity
@Table(name = "product_categories", indexes = {
    @Index(name = "idx_categories_code",      columnList = "code"),
    @Index(name = "idx_categories_parent_id", columnList = "parent_id")
})
@Getter @Setter @NoArgsConstructor
public class ProductCategory extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;        // ex: "ALIM-BOISS"

    @Column(nullable = false, length = 150)
    private String name;        // ex: "Boissons"

    @Column(length = 300)
    private String description;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;

    // ── Auto-référence parent/enfant ──────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCategory> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    // ── Constructeur pratique ─────────────────────────────────────
    public ProductCategory(String code, String name, ProductCategory parent) {
        this.code   = code;
        this.name   = name;
        this.parent = parent;
    }

    // ── Logique métier ────────────────────────────────────────────

    public boolean isRootCategory() { return parent == null; }

    public boolean hasChildren() { return children != null && !children.isEmpty(); }

    /** Retourne le chemin complet : "Alimentaire > Boissons > Eau minérale" */
    public String getFullPath() {
        if (parent == null) return name;
        return parent.getFullPath() + " > " + name;
    }

    public int getDepth() {
        if (parent == null) return 0;
        return 1 + parent.getDepth();
    }
}
