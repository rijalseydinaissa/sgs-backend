package com.example.sgs_backend.domain.common.valueobject;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object — Montant monétaire.
 *
 * Représente une valeur monétaire avec sa devise.
 * Immuable : toutes les opérations retournent un NOUVEAU Money.
 *
 * Utilisé dans : Expense (montant), Product (prix achat/vente),
 *                Invoice (total HT/TTC), SaleOrder (total commande)
 *
 * @Embeddable : JPA intègre ces colonnes directement dans la table
 * de l'entité qui contient ce VO (pas de table séparée).
 */
@Embeddable
public class Money {

    private BigDecimal amount;
    private String currency;

    // ── Constructeur JPA (requis) ─────────────────────────────────
    protected Money() {}

    // ── Constructeur principal ────────────────────────────────────
    private Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif : " + amount);
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("La devise ne peut pas être vide");
        }
        // Toujours 2 décimales — ex: 1500.00 FCFA
        this.amount   = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase().trim();
    }

    // ── Factories ─────────────────────────────────────────────────

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public static Money of(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money ofXOF(BigDecimal amount) {
        return new Money(amount, "XOF");    // Franc CFA Ouest-Africain
    }

    public static Money ofXOF(double amount) {
        return new Money(BigDecimal.valueOf(amount), "XOF");
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    // ── Opérations arithmétiques (retournent un nouveau Money) ────

    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        assertSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Soustraction impossible : résultat négatif (" + result + " " + currency + ")"
            );
        }
        return new Money(result, this.currency);
    }

    public Money multiply(int factor) {
        if (factor < 0) throw new IllegalArgumentException("Facteur ne peut pas être négatif");
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Facteur ne peut pas être négatif");
        }
        return new Money(this.amount.multiply(factor), this.currency);
    }

    /** Applique une remise en pourcentage (ex: 10 = 10%) */
    public Money applyDiscount(BigDecimal discountPercent) {
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0 ||
                discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Remise doit être entre 0 et 100%");
        }
        BigDecimal factor = BigDecimal.ONE.subtract(
                discountPercent.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        );
        return new Money(this.amount.multiply(factor), this.currency);
    }

    /** Calcule le montant TTC depuis un montant HT + taux TVA */
    public Money addTax(BigDecimal taxPercent) {
        BigDecimal factor = BigDecimal.ONE.add(
                taxPercent.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        );
        return new Money(this.amount.multiply(factor), this.currency);
    }

    // ── Comparaisons ──────────────────────────────────────────────

    public boolean isGreaterThan(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    // ── Getters ───────────────────────────────────────────────────

    public BigDecimal getAmount()   { return amount; }
    public String getCurrency()     { return currency; }

    // ── Validation interne ────────────────────────────────────────

    private void assertSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Opération impossible entre devises différentes : " + this.currency + " et " + other.currency
            );
        }
    }

    // ── Equals / HashCode — basés sur la VALEUR (pas sur l'id) ───

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money other)) return false;
        return Objects.equals(amount, other.amount) &&
                Objects.equals(currency, other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency;
        // Exemple : "15000.00 XOF"
    }
}
