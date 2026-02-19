package com.example.sgs_backend.domain.product;

/**
 * Types de mouvements de stock — utilisé dans StockMovement (Sprint 4).
 * Défini ici dans le domain/product car lié au cycle de vie produit.
 */
public enum MovementType {
    ENTRY,    // Entrée : réception commande fournisseur
    EXIT,     // Sortie : vente client
    TRANSFER, // Transfert inter-sites
    LOSS,     // Perte : vol, casse, péremption
    RETURN,   // Retour client ou fournisseur
    ADJUST,   // Ajustement inventaire (correction écart)
    INITIAL   // Stock initial (premier chargement)
}
