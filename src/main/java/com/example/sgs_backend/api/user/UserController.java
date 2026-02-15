package com.example.sgs_backend.api.user;

import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.user.UserService;
import com.example.sgs_backend.application.user.dto.UpdateUserRequest;
import com.example.sgs_backend.application.user.dto.UserRequest;
import com.example.sgs_backend.application.user.dto.UserResponse;
import com.example.sgs_backend.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * ✅ extends BaseController<User, UUID, UserRequest, UserResponse> (Sprint 1)
 *
 * AVANT (❌ Sprint 2 initial) :
 *   class UserController { // tout réimplémenté }
 *   → findAll(), findById(), create(), update(), delete() écrits à la main
 *
 * APRÈS (✅ ce fichier) :
 *   BaseController fournit GRATUITEMENT :
 *     GET  /api/v1/users          → liste paginée (tri, taille configurable)
 *     GET  /api/v1/users/{id}     → un utilisateur
 *     POST /api/v1/users          → créer
 *     PUT  /api/v1/users/{id}     → modifier complet
 *     DELETE /api/v1/users/{id}   → soft delete
 *     GET  /api/v1/users/count    → total
 *
 *   On ajoute UNIQUEMENT les endpoints spécifiques à User.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Gestion des utilisateurs (Admin uniquement)")
public class UserController extends BaseController<User, UUID, UserRequest, UserResponse> {

    private final UserService userService;

    // ── Fournir le service au BaseController ──────────────────────

    @Override
    protected BaseService<User, UUID, UserRequest, UserResponse> getService() {
        return userService;   // ✅ UserService extends BaseService → compatible
    }

    // ── Endpoints supplémentaires spécifiques à User ──────────────
    // (en plus des 6 endpoints hérités de BaseController)

    @GetMapping("/by-username/{username}")
    @Operation(summary = "Trouver un utilisateur par son nom d'utilisateur")
    public ResponseEntity<ApiResponse<UserResponse>> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(ApiResponse.success(userService.findByUsername(username)));
    }

    @PutMapping("/{id}/partial")
    @Operation(summary = "Modifier partiellement un utilisateur (sans changer le mot de passe)")
    public ResponseEntity<ApiResponse<UserResponse>> updatePartial(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.updatePartial(id, request)));
    }

    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Activer ou désactiver un compte")
    public ResponseEntity<ApiResponse<UserResponse>> toggleStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(userService.toggleStatus(id)));
    }

    @PatchMapping("/{id}/unlock")
    @Operation(summary = "Déverrouiller un compte bloqué après trop de tentatives")
    public ResponseEntity<ApiResponse<UserResponse>> unlock(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(userService.unlockAccount(id)));
    }
}
