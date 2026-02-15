package com.example.sgs_backend.application.common;

import com.example.sgs_backend.domain.common.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Port générique de persistance — couche Application.
 *
 * IMPORTANT : Cette interface est dans Application, PAS dans Infrastructure.
 * Aucune dépendance JPA ici. L'Infrastructure fournit les implémentations.
 * C'est le principe Dependency Inversion (DIP) du SOLID.
 *
 * @param <T>  Type de l'entité domaine
 * @param <ID> Type de l'identifiant (UUID dans notre cas)
 */
public interface BaseRepository<T extends BaseEntity, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    /** Recherche en excluant les entités soft-deleted */
    Optional<T> findByIdAndDeletedFalse(ID id);

    List<T> findAllByDeletedFalse();

    Page<T> findAllByDeletedFalse(Pageable pageable);

    /** Soft delete : marque deleted=true, jamais de DELETE SQL */
    void softDelete(ID id);

    boolean existsByIdAndDeletedFalse(ID id);

    long countByDeletedFalse();
}
