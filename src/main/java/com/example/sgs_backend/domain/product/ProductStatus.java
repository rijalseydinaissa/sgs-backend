package com.example.sgs_backend.domain.product;

public enum ProductStatus {
    ACTIVE,       // Disponible à la vente et en stock
    INACTIVE,     // Désactivé temporairement
    ARCHIVED,     // Archivé définitivement (historique conservé)
    OUT_OF_STOCK  // Stock épuisé (calculé automatiquement)
}
