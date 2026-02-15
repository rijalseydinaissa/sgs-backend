package com.example.sgs_backend.api.common;


import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.common.PageResponse;
import com.example.sgs_backend.domain.common.BaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST générique — expose le CRUD standard pour toutes les ressources.
 *
 * Chaque contrôleur spécifique hérite de cette classe et obtient GRATUITEMENT :
 *   GET  /api/v1/{resource}          → liste paginée
 *   GET  /api/v1/{resource}/{id}     → une ressource
 *   POST /api/v1/{resource}          → créer
 *   PUT  /api/v1/{resource}/{id}     → modifier
 *   DELETE /api/v1/{resource}/{id}   → soft delete
 *
 * Les contrôleurs enfants peuvent override n'importe quelle méthode.
 *
 * @param <REQ> DTO Request
 * @param <RES> DTO Response
 * @param <ID>  Type identifiant
 */
public abstract class BaseController<T extends BaseEntity, ID, REQ, RES> {

    protected abstract BaseService<T, ID, REQ, RES> getService();

    // ── GET /  — Liste paginée ────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Lister toutes les ressources (paginé)")
    public ResponseEntity<ApiResponse<PageResponse<RES>>> findAll(
            @RequestParam(defaultValue = "0")   @Parameter(description = "Numéro de page (0-based)") int page,
            @RequestParam(defaultValue = "20")  @Parameter(description = "Taille de la page") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "Champ de tri") String sortBy,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "Direction : ASC ou DESC") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(dir, sortBy));
        return ResponseEntity.ok(ApiResponse.success(getService().findAll(pageable)));
    }

    // ── GET /{id} — Une ressource ─────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une ressource par son identifiant")
    public ResponseEntity<ApiResponse<RES>> findById(@PathVariable ID id) {
        return ResponseEntity.ok(ApiResponse.success(getService().findById(id)));
    }

    // ── POST / — Créer ────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Créer une nouvelle ressource")
    public ResponseEntity<ApiResponse<RES>> create(@Valid @RequestBody REQ request) {
        RES created = getService().create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    // ── PUT /{id} — Modifier ──────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une ressource existante")
    public ResponseEntity<ApiResponse<RES>> update(
            @PathVariable ID id,
            @Valid @RequestBody REQ request
    ) {
        return ResponseEntity.ok(ApiResponse.success(getService().update(id, request)));
    }

    // ── DELETE /{id} — Soft Delete ────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une ressource (soft delete)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        getService().delete(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    // ── Utilitaire ────────────────────────────────────────────────────────────

    @GetMapping("/count")
    @Operation(summary = "Nombre total de ressources")
    public ResponseEntity<ApiResponse<Long>> count() {
        return ResponseEntity.ok(ApiResponse.success(getService().count()));
    }
}
