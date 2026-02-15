package com.example.sgs_backend.domain.common.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object — Informations de contact (email + téléphone).
 *
 * Immuable. Valide le format email et téléphone à la construction.
 * Si invalide → exception levée immédiatement (Fail Fast).
 *
 * Utilisé dans :
 *   - User       (email de connexion + téléphone)
 *   - Customer   (email + téléphone du client)
 *   - Supplier   (email + téléphone du fournisseur)
 *   - Site       (email + téléphone du site)
 */
@Embeddable
public class ContactInfo {

    // Regex email RFC 5322 simplifiée
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );

    // Accepte formats internationaux : +221 77 123 45 67 / 0033612345678 / 77 123 45 67
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[0-9\\s\\-().]{7,20}$"
    );

    private String email;
    private String phone;
    private String whatsapp;   // Numéro WhatsApp (souvent différent du tel principal)

    // ── Constructeur JPA (requis) ─────────────────────────────────
    protected ContactInfo() {}

    // ── Constructeur privé ────────────────────────────────────────
    private ContactInfo(String email, String phone, String whatsapp) {
        // Email requis
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Format email invalide : " + email);
        }

        // Téléphone optionnel mais validé si fourni
        if (phone != null && !phone.isBlank() && !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new IllegalArgumentException("Format téléphone invalide : " + phone);
        }

        if (whatsapp != null && !whatsapp.isBlank() && !PHONE_PATTERN.matcher(whatsapp.trim()).matches()) {
            throw new IllegalArgumentException("Format WhatsApp invalide : " + whatsapp);
        }

        this.email    = email.trim().toLowerCase();
        this.phone    = phone    != null ? phone.trim()    : null;
        this.whatsapp = whatsapp != null ? whatsapp.trim() : null;
    }

    // ── Factories ─────────────────────────────────────────────────

    public static ContactInfo of(String email, String phone, String whatsapp) {
        return new ContactInfo(email, phone, whatsapp);
    }

    public static ContactInfo of(String email, String phone) {
        return new ContactInfo(email, phone, null);
    }

    public static ContactInfo ofEmail(String email) {
        return new ContactInfo(email, null, null);
    }

    // ── Utilitaires ───────────────────────────────────────────────

    public boolean hasPhone() {
        return phone != null && !phone.isBlank();
    }

    public boolean hasWhatsapp() {
        return whatsapp != null && !whatsapp.isBlank();
    }

    /**
     * Masque l'email pour affichage sécurisé.
     * Ex : "m***@gmail.com"
     */
    public String getMaskedEmail() {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return email;
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    /**
     * Masque le téléphone pour affichage sécurisé.
     * Ex : "+221 77 ***-45-67"
     */
    public String getMaskedPhone() {
        if (!hasPhone()) return null;
        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() < 4) return phone;
        return phone.substring(0, phone.length() - 4) + "****";
    }

    /** Valide statiquement un email sans créer d'objet */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /** Valide statiquement un téléphone sans créer d'objet */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    // ── Getters ───────────────────────────────────────────────────

    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }
    public String getWhatsapp() { return whatsapp; }

    // ── Equals / HashCode — basés sur la VALEUR ──────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactInfo other)) return false;
        return Objects.equals(email, other.email) &&
                Objects.equals(phone, other.phone) &&
                Objects.equals(whatsapp, other.whatsapp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phone, whatsapp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ContactInfo{email='").append(email).append("'");
        if (hasPhone()) sb.append(", phone='").append(phone).append("'");
        if (hasWhatsapp()) sb.append(", whatsapp='").append(whatsapp).append("'");
        return sb.append("}").toString();
    }
}

