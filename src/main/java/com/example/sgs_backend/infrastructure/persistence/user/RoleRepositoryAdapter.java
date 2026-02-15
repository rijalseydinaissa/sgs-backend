package com.example.sgs_backend.infrastructure.persistence.user;

import com.example.sgs_backend.application.user.port.RoleRepository;
import com.example.sgs_backend.domain.user.Role;
import com.example.sgs_backend.domain.user.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository jpa;

    @Override public Role save(Role r)                               { return jpa.save(r); }
    @Override public Optional<Role> findById(UUID id)                { return jpa.findById(id); }
    @Override public Optional<Role> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Role> findAllByDeletedFalse()              { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Role> findAllByDeletedFalse(Pageable p)    { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id)      { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse()                      { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id)                        { jpa.softDeleteById(id); }

    @Override public Optional<Role> findByName(RoleName n) { return jpa.findByName(n); }
    @Override public boolean existsByName(RoleName n)      { return jpa.existsByName(n); }
}
