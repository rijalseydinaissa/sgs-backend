package com.example.sgs_backend.domain.user;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.nio.file.FileStore;
import java.util.HashSet;
import java.util.Set;

/**
 * ✅ extends BaseEntity (Sprint 1)
 */
@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 40)
    private RoleName name;

    @Column(length = 200)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name               = "role_permissions",
        joinColumns        = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    public Role(RoleName name, String description) {
        this.name = name; this.description = description;
    }

    public void addPermission(Permission p)        { this.permissions.add(p); }
    public boolean hasPermission(PermissionName n) {
        return permissions.stream().anyMatch(p -> p.getName() == n);
    }
}
