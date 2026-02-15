package com.example.sgs_backend.api.common;

/**
 * Constantes pour tous les codes d'erreur retournés par l'API.
 * Permet au frontend de gérer les erreurs de façon programmatique.
 */
public final class ApiErrorCode {

    private ApiErrorCode() {}

    // Ressources
    public static final String NOT_FOUND         = "ERR_NOT_FOUND";
    public static final String DUPLICATE         = "ERR_DUPLICATE";

    // Validation
    public static final String VALIDATION        = "ERR_VALIDATION";
    public static final String INVALID_FORMAT    = "ERR_INVALID_FORMAT";

    // Métier
    public static final String BUSINESS_RULE     = "ERR_BUSINESS_RULE";
    public static final String INSUFFICIENT_STOCK= "ERR_INSUFFICIENT_STOCK";

    // Sécurité
    public static final String UNAUTHORIZED      = "ERR_UNAUTHORIZED";
    public static final String FORBIDDEN         = "ERR_FORBIDDEN";
    public static final String TOKEN_EXPIRED     = "ERR_TOKEN_EXPIRED";
    public static final String TOKEN_INVALID     = "ERR_TOKEN_INVALID";
    public static final String INVALID_2FA       = "ERR_INVALID_2FA";

    // Système
    public static final String INTERNAL          = "ERR_INTERNAL";
    public static final String SERVICE_UNAVAILABLE = "ERR_SERVICE_UNAVAILABLE";
}
