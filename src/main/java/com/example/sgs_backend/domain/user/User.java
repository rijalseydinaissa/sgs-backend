package com.example.sgs_backend.domain.user;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * ✅ extends BaseEntity (Sprint 1)
 *
 * AVANT (❌ Sprint 2 initial) :
 *   @Id @GeneratedValue UUID id;
 *   @CreatedDate LocalDateTime createdAt;
 *   @LastModifiedDate LocalDateTime updatedAt;
 *   @CreatedBy String createdBy;
 *   @LastModifiedBy String updatedBy;
 *   @Version Long version;
 *   boolean deleted;
 *   → 7 champs copiés/collés depuis BaseEntity = VIOLATION DRY
 *
 * APRÈS (✅ ce fichier) :
 *   → Tout ça vient de BaseEntity, RIEN à redéfinir ici.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username"),
    @Index(name = "idx_users_email",    columnList = "email"),
    @Index(name = "idx_users_status",   columnList = "status")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 80)
    private String firstName;

    @Column(name = "last_name", length = 80)
    private String lastName;

    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    // ── 2FA ────────────────────────────────────────────────────────
    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled = false;

    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    // ── Sécurité brute-force ───────────────────────────────────────
    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    // ── Relations ──────────────────────────────────────────────────
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name               = "user_roles",
        joinColumns        = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "site_id")
    private UUID siteId;

    // ── Logique métier (dans le Domain, pas dans les Services) ─────

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.status      = UserStatus.LOCKED;
            this.lockedUntil = LocalDateTime.now().plusMinutes(30);
        }
    }

    public void recordSuccessfulLogin(String ip) {
        this.failedLoginAttempts = 0;
        this.lockedUntil         = null;
        if (this.status == UserStatus.LOCKED) this.status = UserStatus.ACTIVE;
        this.lastLoginAt = LocalDateTime.now();
        this.lastLoginIp = ip;
    }

    public boolean isAccountLocked() {
        if (status == UserStatus.LOCKED && lockedUntil != null) {
            if (LocalDateTime.now().isAfter(lockedUntil)) {
                this.status = UserStatus.ACTIVE;
                this.failedLoginAttempts = 0;
                this.lockedUntil = null;
                return false;
            }
            return true;
        }
        return status == UserStatus.LOCKED;
    }

    public boolean isActive() { return status == UserStatus.ACTIVE; }

    public String getFullName() {
        if (firstName == null && lastName == null) return username;
        return ((firstName != null ? firstName : "") + " " +
                (lastName  != null ? lastName  : "")).trim();
    }

    public void addRole(Role role)                  { this.roles.add(role); }
    public boolean hasRole(RoleName rn)             { return roles.stream().anyMatch(r -> r.getName() == rn); }
    public boolean hasPermission(PermissionName pn) { return roles.stream().anyMatch(r -> r.hasPermission(pn)); }
}
