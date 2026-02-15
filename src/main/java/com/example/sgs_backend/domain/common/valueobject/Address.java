package com.example.sgs_backend.domain.common.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Value Object — Adresse postale structurée.
 *
 * Immuable. Intégré directement dans les entités qui en ont besoin.
 *
 * Utilisé dans :
 *   - Supplier   (adresse du fournisseur)
 *   - Customer   (adresse de livraison / facturation)
 *   - Site       (adresse du magasin/entrepôt)
 *   - DeliveryNote (adresse de livraison)
 *
 * JPA : les colonnes street, city, etc. sont créées dans la table
 * de l'entité parente avec des noms préfixés configurables via @AttributeOverride.
 *
 * Exemple dans une Entity :
 * <pre>
 *   @Embedded
 *   @AttributeOverrides({
 *       @AttributeOverride(name = "street",  column = @Column(name = "billing_street")),
 *       @AttributeOverride(name = "city",    column = @Column(name = "billing_city")),
 *   })
 *   private Address billingAddress;
 * </pre>
 */
@Embeddable
public class Address {

    private String street;      // Rue / Quartier
    private String city;        // Ville
    private String region;      // Région / Département
    private String postalCode;  // Code postal (optionnel en Afrique)
    private String country;     // Pays

    // ── Constructeur JPA (requis) ─────────────────────────────────
    protected Address() {}

    // ── Constructeur principal ────────────────────────────────────
    private Address(String street, String city, String region,
                    String postalCode, String country) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("La ville est obligatoire");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Le pays est obligatoire");
        }
        this.street     = street != null ? street.trim() : null;
        this.city       = city.trim();
        this.region     = region != null ? region.trim() : null;
        this.postalCode = postalCode != null ? postalCode.trim() : null;
        this.country    = country.trim();
    }

    // ── Factories ─────────────────────────────────────────────────

    /** Adresse complète */
    public static Address of(String street, String city, String region,
                             String postalCode, String country) {
        return new Address(street, city, region, postalCode, country);
    }

    /** Adresse simplifiée (Afrique de l'Ouest — pas toujours de code postal) */
    public static Address of(String street, String city, String country) {
        return new Address(street, city, null, null, country);
    }

    /** Adresse minimale (ville + pays uniquement) */
    public static Address ofCity(String city, String country) {
        return new Address(null, city, null, null, country);
    }

    // ── Utilitaires ───────────────────────────────────────────────

    /**
     * Retourne l'adresse formatée sur une ligne.
     * Ex : "Almadies, Dakar, Sénégal"
     */
    public String toOneLine() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isBlank()) sb.append(street).append(", ");
        sb.append(city);
        if (region != null && !region.isBlank()) sb.append(", ").append(region);
        if (postalCode != null && !postalCode.isBlank()) sb.append(" ").append(postalCode);
        sb.append(", ").append(country);
        return sb.toString();
    }

    /**
     * Retourne l'adresse formatée sur plusieurs lignes (pour impression).
     * Ex :
     *   Rue 10, Almadies
     *   Dakar 12500
     *   Sénégal
     */
    public String toMultiLine() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isBlank()) sb.append(street).append("\n");
        sb.append(city);
        if (postalCode != null && !postalCode.isBlank()) sb.append(" ").append(postalCode);
        sb.append("\n").append(country);
        return sb.toString();
    }

    public boolean isInCountry(String countryName) {
        return this.country.equalsIgnoreCase(countryName);
    }

    // ── Getters ───────────────────────────────────────────────────

    public String getStreet()     { return street; }
    public String getCity()       { return city; }
    public String getRegion()     { return region; }
    public String getPostalCode() { return postalCode; }
    public String getCountry()    { return country; }

    // ── Equals / HashCode — basés sur la VALEUR ──────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address other)) return false;
        return Objects.equals(street, other.street) &&
                Objects.equals(city, other.city) &&
                Objects.equals(region, other.region) &&
                Objects.equals(postalCode, other.postalCode) &&
                Objects.equals(country, other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, region, postalCode, country);
    }

    @Override
    public String toString() {
        return toOneLine();
    }
}

