package com.example.sgs_backend.application.user;

import com.example.sgs_backend.application.auth.port.UserRepository;
import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.user.dto.UpdateUserRequest;
import com.example.sgs_backend.application.user.dto.UserRequest;
import com.example.sgs_backend.application.user.dto.UserResponse;
import com.example.sgs_backend.application.user.port.RoleRepository;
import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.example.sgs_backend.domain.common.exception.DuplicateResourceException;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import com.example.sgs_backend.domain.user.Role;
import com.example.sgs_backend.domain.user.RoleName;
import com.example.sgs_backend.domain.user.User;
import com.example.sgs_backend.domain.user.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ✅ extends BaseService<User, UUID, UserRequest, UserResponse> (Sprint 1)
 *
 * AVANT (❌ Sprint 2 initial) :
 *   class UserService { // service custom, tout réimplémenté }
 *   → create(), findById(), update(), delete(), findAll() écrits à la main
 *
 * APRÈS (✅ ce fichier) :
 *   BaseService fournit : create, findById, update, delete, findAll, count
 *   On implémente UNIQUEMENT les 4 méthodes abstraites + les cas spéciaux.
 *
 * Pattern Template Method — BaseService appelle nos implémentations
 * de toEntity(), toResponse(), updateEntity(), getEntityName()
 */
@Service
@Slf4j
public class UserService extends BaseService<User, UUID, UserRequest, UserResponse> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.roleRepository  = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ── 4 méthodes abstraites obligatoires ────────────────────────

    @Override
    protected BaseRepository<User, UUID> getRepository() {
        return userRepository;   // ✅ UserRepository extends BaseRepository
    }

    @Override
    protected String getEntityName() { return "User"; }

    @Override
    protected User toEntity(UserRequest request) {
        // Vérifications unicité AVANT de créer l'entité (Fail Fast)
        if (userRepository.existsByUsername(request.username()))
            throw new DuplicateResourceException("User", "username", request.username());
        if (userRepository.existsByEmail(request.email()))
            throw new DuplicateResourceException("User", "email", request.email());

        User user = User.builder()
                .username(request.username())
                .email(request.email().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .status(UserStatus.ACTIVE)
                .siteId(request.siteId())
                .build();

        // Associer les rôles demandés
        resolveRoles(request.roles()).forEach(user::addRole);
        return user;
    }

    @Override
    protected UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());
        return new UserResponse(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getFullName(),
                user.getPhone(), user.getStatus(), user.isTwoFactorEnabled(),
                roles, user.getSiteId(), null,
                user.getLastLoginAt(), user.getCreatedAt(), user.getCreatedBy()
        );
    }

    @Override
    protected void updateEntity(User user, UserRequest request) {
        // Vérifier unicité email si changé
        if (!user.getEmail().equalsIgnoreCase(request.email())
                && userRepository.existsByEmail(request.email()))
            throw new DuplicateResourceException("User", "email", request.email());

        user.setEmail(request.email().toLowerCase());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setSiteId(request.siteId());

        if (request.roles() != null && !request.roles().isEmpty()) {
            user.getRoles().clear();
            resolveRoles(request.roles()).forEach(user::addRole);
        }
    }

    // ── Méthodes supplémentaires spécifiques à User ───────────────
    // (non couvertes par BaseService)

    /** Modification partielle via UpdateUserRequest (différent de UserRequest) */
    @Transactional
    public UserResponse updatePartial(UUID id, UpdateUserRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (!user.getEmail().equalsIgnoreCase(request.email())
                && userRepository.existsByEmail(request.email()))
            throw new DuplicateResourceException("User", "email", request.email());

        user.setEmail(request.email().toLowerCase());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setSiteId(request.siteId());

        if (request.roles() != null && !request.roles().isEmpty()) {
            user.getRoles().clear();
            resolveRoles(request.roles()).forEach(user::addRole);
        }
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse toggleStatus(UUID id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (user.getStatus() == UserStatus.ACTIVE)
            user.setStatus(UserStatus.INACTIVE);
        else if (user.getStatus() == UserStatus.INACTIVE)
            user.setStatus(UserStatus.ACTIVE);
        else
            throw new BusinessRuleException("Impossible de modifier le statut d'un compte verrouillé");
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse unlockAccount(UUID id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setStatus(UserStatus.ACTIVE);
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        log.info("Compte déverrouillé par admin : {}", user.getUsername());
        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

    // ── Helper privé ──────────────────────────────────────────────
    private Set<Role> resolveRoles(Set<RoleName> roleNames) {
        return roleNames.stream()
                .map(rn -> roleRepository.findByName(rn)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", rn)))
                .collect(Collectors.toSet());
    }
}
