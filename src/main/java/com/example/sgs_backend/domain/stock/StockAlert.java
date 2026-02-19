package com.example.sgs_backend.domain.stock;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ✅ extends BaseEntity
 * Alertes stock générées automatiquement :
 *   - Rupture (stock = 0)
 *   - Stock faible (stock <= seuil min)
 *   - Stock excédent (stock > seuil max)
 *   - Expiration proche
 */
@Entity
@Table(name = "stock_alerts", indexes = {
    @Index(name = "idx_alerts_product", columnList = "product_id"),
    @Index(name = "idx_alerts_type",    columnList = "alert_type"),
    @Index(name = "idx_alerts_status",  columnList = "status")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class StockAlert extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, length = 30)
    private AlertType alertType;

    @Column(nullable = false, length = 255)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status = AlertStatus.ACTIVE;

    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by_user_id")
    private UUID resolvedByUserId;

    // ── Logique métier ────────────────────────────────────────────

    public void resolve(UUID userId) {
        this.status         = AlertStatus.RESOLVED;
        this.resolvedAt     = LocalDateTime.now();
        this.resolvedByUserId = userId;
    }

    public boolean isActive() { return status == AlertStatus.ACTIVE; }
}

enum AlertType {
    OUT_OF_STOCK,       // Stock épuisé
    LOW_STOCK,          // Stock sous le seuil minimum
    EXCESS_STOCK,       // Stock au-dessus du maximum
    EXPIRING_SOON,      // Produit expire bientôt
    REORDER_POINT       // Seuil de réapprovisionnement atteint
}

enum AlertStatus {
    ACTIVE,    // En attente de traitement
    RESOLVED,  // Résolue
    IGNORED    // Ignorée volontairement
}
