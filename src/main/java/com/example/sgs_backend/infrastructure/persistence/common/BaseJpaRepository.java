package com.example.sgs_backend.infrastructure.persistence.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository JPA de base — étend JpaRepository avec les méthodes soft-delete.
 *
 * @NoRepositoryBean : Spring Data ne crée PAS d'implémentation pour cette interface.
 * Chaque repository spécifique l'étend en précisant son entité JPA.
 */
@NoRepositoryBean
public interface BaseJpaRepository<E, ID> extends JpaRepository<E, ID> {

    Optional<E> findByIdAndDeletedFalse(ID id);

    List<E> findAllByDeletedFalse();

    Page<E> findAllByDeletedFalse(Pageable pageable);

    boolean existsByIdAndDeletedFalse(ID id);

    long countByDeletedFalse();

    /** Soft delete en une seule requête UPDATE — plus efficace qu'un find+save */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") ID id);
}
