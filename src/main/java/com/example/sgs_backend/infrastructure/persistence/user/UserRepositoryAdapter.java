package com.example.sgs_backend.infrastructure.persistence.user;

import com.example.sgs_backend.application.auth.port.UserRepository;
import com.example.sgs_backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur qui implémente UserRepository (couche Application)
 * en déléguant à UserJpaRepository (couche Infrastructure).
 *
 * ✅ Implémente toutes les méthodes de BaseRepository<User, UUID>
 *    + les méthodes spécifiques à UserRepository
 *
 * Pas de mapper User ↔ JpaEntity ici car User extends BaseEntity
 * directement — JPA l'utilise nativement. Plus besoin de classe
 * intermédiaire UserJpaEntity avec duplication de champs !
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;

    // ── Méthodes de BaseRepository<User, UUID> ────────────────────

    @Override public User save(User user)                             { return jpa.save(user); }
    @Override public Optional<User> findById(UUID id)                { return jpa.findById(id); }
    @Override public Optional<User> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<User> findAllByDeletedFalse()              { return jpa.findAllByDeletedFalse(); }
    @Override public Page<User> findAllByDeletedFalse(Pageable p)    { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id)      { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse()                      { return jpa.countByDeletedFalse(); }

    @Override
    public void softDelete(UUID id) {
        jpa.softDeleteById(id);   // ✅ Méthode de BaseJpaRepository (Sprint 1)
    }

    // ── Méthodes spécifiques à UserRepository ────────────────────

    @Override public Optional<User> findByUsername(String u) { return jpa.findByUsername(u); }
    @Override public Optional<User> findByEmail(String e)    { return jpa.findByEmail(e); }
    @Override public boolean existsByUsername(String u)      { return jpa.existsByUsernameAndDeletedFalse(u); }
    @Override public boolean existsByEmail(String e)         { return jpa.existsByEmailAndDeletedFalse(e); }
}
