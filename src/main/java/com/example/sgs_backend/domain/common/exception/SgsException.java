package com.example.sgs_backend.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception abstraite racine de toutes les exceptions métier SGS.
 * Toutes les exceptions spécifiques l'étendent pour avoir :
 * - Un code d'erreur lisible (ex: ERR_NOT_FOUND)
 * - Un statut HTTP associé
 */
public abstract class SgsException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    protected SgsException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    protected SgsException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() { return errorCode; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}
