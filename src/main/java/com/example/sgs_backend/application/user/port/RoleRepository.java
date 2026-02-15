package com.example.sgs_backend.application.user.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.user.Role;
import com.example.sgs_backend.domain.user.RoleName;

import java.util.Optional;
import java.util.UUID;

/**
 * ✅ extends BaseRepository<Role, UUID> (Sprint 1)
 */
public interface RoleRepository extends BaseRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
