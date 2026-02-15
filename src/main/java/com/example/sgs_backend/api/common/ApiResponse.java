package com.example.sgs_backend.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Enveloppe standard pour TOUTES les réponses de l'API SGS.
 *
 * Format uniforme :
 * {
 *   "success": true/false,
 *   "data": { ... },          // présent si success=true
 *   "message": "...",         // toujours présent
 *   "errorCode": "...",       // présent si success=false
 *   "timestamp": "..."
 * }
 *
 * Les champs null sont exclus du JSON (@JsonInclude).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Réponse standard de l'API SGS")
public record ApiResponse<T>(

        @Schema(description = "Indique si l'opération a réussi")
        boolean success,

        @Schema(description = "Données retournées (null si erreur)")
        T data,

        @Schema(description = "Message informatif ou description de l'erreur")
        String message,

        @Schema(description = "Code d'erreur technique (null si succès)")
        String errorCode,

        @Schema(description = "Horodatage de la réponse")
        Instant timestamp

) {
    // ── Factories ─────────────────────────────────────────────────────────────

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Opération réussie", null, Instant.now());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null, Instant.now());
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, data, "Ressource créée avec succès", null, Instant.now());
    }

    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(true, null, "Opération effectuée", null, Instant.now());
    }

    public static ApiResponse<Void> error(String message, String errorCode) {
        return new ApiResponse<>(false, null, message, errorCode, Instant.now());
    }
}
