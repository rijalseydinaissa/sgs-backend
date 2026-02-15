package com.example.sgs_backend.infrastructure.persistence.user;


import com.example.sgs_backend.domain.user.User;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * ✅ extends BaseJpaRepository<User, UUID> (Sprint 1)
 *
 * AVANT (❌) : extends JpaRepository<UserJpaEntity, UUID>
 *   → devait redéfinir findByIdAndDeletedFalse, findAllByDeletedFalse, softDeleteById...
 *
 * APRÈS (✅) : extends BaseJpaRepository
 *   → toutes ces méthodes héritées automatiquement
 *   → on ajoute UNIQUEMENT findByUsername et findByEmail
 *
 * ATTENTION : ici on travaille directement avec l'entité domain User
 * (qui étend BaseEntity) — plus besoin d'une JpaEntity séparée !
 */
@Repository
public interface UserJpaRepository extends BaseJpaRepository<User, UUID> {

    Optional<User> findByUsernameAndDeletedFalse(String username);
    Optional<User> findByEmailAndDeletedFalse(String email);

    // Recherche sans filtre soft-delete (pour l'auth — vérifier même les comptes locked)
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByUsernameAndDeletedFalse(String username);
    boolean existsByEmailAndDeletedFalse(String email);
}
