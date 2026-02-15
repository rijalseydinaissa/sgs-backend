package com.example.sgs_backend.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Levée quand une règle métier est violée.
 * Exemples : approuver une dépense déjà approuvée, stock négatif...
 * Produit un HTTP 422 Unprocessable Entity.
 */
public class BusinessRuleException extends SgsException {

    public BusinessRuleException(String message) {
        super(message, "ERR_BUSINESS_RULE", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessRuleException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
