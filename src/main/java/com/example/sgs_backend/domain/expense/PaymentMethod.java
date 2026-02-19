package com.example.sgs_backend.domain.expense;

import io.jsonwebtoken.security.KeyOperationBuilder;

public enum PaymentMethod {
    CASH,           // Espèces
    BANK_TRANSFER,  // Virement bancaire
    MOBILE_MONEY,   // Orange Money, Wave, Free Money
    CHECK,          // Chèque
    CREDIT_CARD     // Carte bancaire
}
