package com.example.sgs_backend.application.product.port;

/**
 * Port pour la génération de codes-barres.
 * Implémenté dans Infrastructure (ZXing / iText).
 */
public interface BarcodeGeneratorPort {
    /** Génère un EAN-13 unique à partir de la référence produit */
    String generateEan13(String productReference);
    /** Génère un QR Code (contenu JSON ou URL) */
    String generateQrCode(String content);
    /** Génère une image PNG du code-barres en Base64 */
    String generateBarcodeImageBase64(String code, String type);
}
