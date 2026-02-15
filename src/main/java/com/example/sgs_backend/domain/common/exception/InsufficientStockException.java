package com.example.sgs_backend.domain.common.exception;

/**
 * Levée quand le stock disponible est insuffisant pour un mouvement.
 */
public class InsufficientStockException extends BusinessRuleException {

    public InsufficientStockException(String productName, int available, int requested) {
        super(
            String.format(
                "Stock insuffisant pour '%s' : %d disponible, %d demandé",
                productName, available, requested
            ),
            "ERR_INSUFFICIENT_STOCK"
        );
    }
}
