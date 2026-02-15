package com.example.sgs_backend.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Levée quand une ressource avec la même référence/clé unique existe déjà.
 * Produit un HTTP 409 Conflict.
 */
public class DuplicateResourceException extends SgsException {

    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(
            String.format("%s avec %s='%s' existe déjà", resourceName, field, value),
            "ERR_DUPLICATE",
            HttpStatus.CONFLICT
        );
    }
}
