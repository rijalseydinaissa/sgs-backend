package com.example.sgs_backend.infrastructure.persistence.product;

import com.example.sgs_backend.application.product.port.BarcodeGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implémentation simple du générateur de codes-barres.
 * En production : utiliser ZXing (com.google.zxing) pour les vraies images.
 */
@Component
public class BarcodeGeneratorAdapter implements BarcodeGeneratorPort {

    private static final AtomicLong counter = new AtomicLong(1000000000000L);

    @Override
    public String generateEan13(String productReference) {
        // Génère un EAN-13 unique basé sur timestamp + compteur
        long base = counter.incrementAndGet();
        String digits = String.valueOf(base).substring(0, 12);
        return digits + calculateEan13CheckDigit(digits);
    }

    @Override
    public String generateQrCode(String content) {
        // Retourne le contenu brut — l'image est générée côté frontend ou iText
        return content;
    }

    @Override
    public String generateBarcodeImageBase64(String code, String type) {
        // Placeholder — à implémenter avec ZXing en production
        return "data:image/png;base64,PLACEHOLDER_" + code;
    }

    private int calculateEan13CheckDigit(String digits) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int d = Character.getNumericValue(digits.charAt(i));
            sum += (i % 2 == 0) ? d : d * 3;
        }
        return (10 - (sum % 10)) % 10;
    }
}
