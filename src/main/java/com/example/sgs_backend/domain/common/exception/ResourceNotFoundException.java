package com.example.sgs_backend.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Levée quand une ressource est introuvable en base.
 * Produit un HTTP 404.
 */
public class ResourceNotFoundException extends SgsException {

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(
            String.format("%s introuvable avec l'identifiant : %s", resourceName, identifier),
            "ERR_NOT_FOUND",
            HttpStatus.NOT_FOUND
        );
    }
}
