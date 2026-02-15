package com.example.sgs_backend.infrastructure.persistence.user;

import com.example.sgs_backend.domain.user.Role;
import com.example.sgs_backend.domain.user.RoleName;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * ✅ extends BaseJpaRepository<Role, UUID> (Sprint 1)
 */
@Repository
public interface RoleJpaRepository extends BaseJpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
