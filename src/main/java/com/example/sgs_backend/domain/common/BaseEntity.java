package com.example.sgs_backend.domain.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe de base pour toutes les entités JPA du système SGS.
 *
 * Fournit automatiquement :
 * - Identifiant UUID auto-généré
 * - Audit (createdAt, updatedAt, createdBy, updatedBy)
 * - Versioning optimiste (évite conflits concurrents)
 * - Soft Delete (jamais de DELETE SQL physique)
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /**
     * Optimistic locking — Spring Data incrémente automatiquement.
     * Si deux transactions modifient la même entité : OptimisticLockException.
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Soft delete : on marque deleted=true au lieu de faire un DELETE SQL.
     * Toutes les requêtes doivent filtrer sur deleted=false.
     */
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // ── Equals / HashCode basés uniquement sur l'id UUID ──────────────────
    // Jamais sur les champs métier (problèmes Hibernate avec collections lazy)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
