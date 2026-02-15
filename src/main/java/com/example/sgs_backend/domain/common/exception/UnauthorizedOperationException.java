package com.example.sgs_backend.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Levée quand l'utilisateur tente une opération non autorisée pour son rôle.
 * Produit un HTTP 403 Forbidden.
 */
public class UnauthorizedOperationException extends SgsException {

    public UnauthorizedOperationException(String operation) {
        super(
            String.format("Opération non autorisée : %s", operation),
            "ERR_FORBIDDEN",
            HttpStatus.FORBIDDEN
        );
    }
}
