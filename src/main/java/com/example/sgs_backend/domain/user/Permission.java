package com.example.sgs_backend.domain.user;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * ✅ extends BaseEntity (Sprint 1)
 * Hérite GRATUITEMENT : id UUID, createdAt, updatedAt,
 *                       createdBy, updatedBy, version, deleted
 * Zéro duplication de code.
 */
@Entity
@Table(name = "permissions")
@Getter @Setter @NoArgsConstructor
public class Permission extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 60)
    private PermissionName name;

    @Column(length = 200)
    private String description;

    public Permission(PermissionName name, String description) {
        this.name        = name;
        this.description = description;
    }
}
