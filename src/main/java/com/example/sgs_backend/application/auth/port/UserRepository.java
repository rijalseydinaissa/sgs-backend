package com.example.sgs_backend.application.auth.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.user.User;

import java.util.Optional;
import java.util.UUID;

/**
 * ✅ extends BaseRepository<User, UUID> (Sprint 1)
 * Hérite : save, findById, findByIdAndDeletedFalse, findAllByDeletedFalse,
 *          softDelete, existsByIdAndDeletedFalse, countByDeletedFalse
 * On ajoute uniquement les méthodes propres à User.
 */
public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
