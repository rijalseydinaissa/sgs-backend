package com.example.sgs_backend.domain.stock;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

/**
 * ✅ extends BaseEntity
 * Session d'inventaire physique pour un site à une date donnée.
 */
@Entity
@Table(name = "inventories", indexes = {
    @Index(name = "idx_inventories_site", columnList = "site_id"),
    @Index(name = "idx_inventories_date", columnList = "inventory_date"),
    @Index(name = "idx_inventories_status", columnList = "status")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Inventory extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String reference;  // ex: INV-2024-001

    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @Column(name = "inventory_date", nullable = false)
    private LocalDate inventoryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InventoryStatus status = InventoryStatus.DRAFT;

    @Column(name = "responsible_user_id")
    private UUID responsibleUserId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryLine> lines = new ArrayList<>();

    // ── Logique métier ────────────────────────────────────────────

    public void addLine(InventoryLine line) {
        line.setInventory(this);
        this.lines.add(line);
    }

    public void validate() {
        if (status != InventoryStatus.DRAFT)
            throw new IllegalStateException("Seul un inventaire DRAFT peut être validé");
        this.status = InventoryStatus.VALIDATED;
    }

    public boolean isDraft()     { return status == InventoryStatus.DRAFT; }
    public boolean isValidated() { return status == InventoryStatus.VALIDATED; }
}

enum InventoryStatus {
    DRAFT,      // En cours de saisie
    VALIDATED,  // Validé — stock ajusté
    CANCELLED   // Annulé
}
