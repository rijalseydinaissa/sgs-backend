package com.example.sgs_backend.domain.common.valueobject;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object — Quantité avec unité de mesure.
 *
 * Immuable. Gère les différentes unités utilisées dans le stock SGS.
 * Toutes les opérations retournent un NOUVEAU Quantity.
 *
 * Utilisé dans :
 *   - StockMovement  (quantité du mouvement)
 *   - Product        (stock actuel, seuil d'alerte, seuil min)
 *   - PurchaseOrderLine (quantité commandée / reçue)
 *   - SaleOrderLine  (quantité vendue)
 *   - InventoryLine  (qté théorique vs qté réelle)
 *
 * Unités supportées :
 *   PIECE (pcs), KG (kilogramme), GRAM (gramme),
 *   LITER (litre), ML (millilitre),
 *   METER (mètre), CM (centimètre),
 *   CARTON (carton/caisse), PACK (pack), BOX (boite)
 */
@Embeddable
public class Quantity {

    // ── Enum des unités de mesure ─────────────────────────────────
    public enum Unit {
        PIECE("pcs",    "Pièce",       false),
        KG   ("kg",     "Kilogramme",  true),
        GRAM ("g",      "Gramme",      true),
        LITER("L",      "Litre",       true),
        ML   ("mL",     "Millilitre",  true),
        METER("m",      "Mètre",       true),
        CM   ("cm",     "Centimètre",  true),
        CARTON("ctn",   "Carton",      false),
        PACK ("pack",   "Pack",        false),
        BOX  ("box",    "Boite",       false);

        private final String symbol;
        private final String label;
        private final boolean allowsDecimals; // Ex: 2.5 kg OK, mais 2.5 pièces NON

        Unit(String symbol, String label, boolean allowsDecimals) {
            this.symbol = symbol;
            this.label = label;
            this.allowsDecimals = allowsDecimals;
        }

        public String getSymbol()          { return symbol; }
        public String getLabel()           { return label; }
        public boolean allowsDecimals()    { return allowsDecimals; }
    }

    private BigDecimal value;
    private Unit unit;

    // ── Constructeur JPA (requis) ─────────────────────────────────
    protected Quantity() {}

    // ── Constructeur privé ────────────────────────────────────────
    private Quantity(BigDecimal value, Unit unit) {
        if (value == null)  throw new IllegalArgumentException("La valeur ne peut pas être null");
        if (unit == null)   throw new IllegalArgumentException("L'unité ne peut pas être null");
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative : " + value);
        }

        // Valider les décimales selon l'unité
        if (!unit.allowsDecimals() && value.scale() > 0 &&
                value.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "L'unité " + unit.getLabel() + " n'accepte pas les décimales. Valeur: " + value
            );
        }

        this.value = unit.allowsDecimals()
                ? value.setScale(3, RoundingMode.HALF_UP)   // 3 décimales pour poids/volumes
                : value.setScale(0, RoundingMode.UNNECESSARY); // Entier pour pièces/cartons
        this.unit = unit;
    }

    // ── Factories ─────────────────────────────────────────────────

    public static Quantity of(BigDecimal value, Unit unit) {
        return new Quantity(value, unit);
    }

    public static Quantity of(int value, Unit unit) {
        return new Quantity(BigDecimal.valueOf(value), unit);
    }

    public static Quantity of(double value, Unit unit) {
        return new Quantity(BigDecimal.valueOf(value), unit);
    }

    // Factories rapides pour les unités les plus courantes
    public static Quantity pieces(int value)       { return of(value, Unit.PIECE);  }
    public static Quantity kg(double value)        { return of(value, Unit.KG);     }
    public static Quantity liters(double value)    { return of(value, Unit.LITER);  }
    public static Quantity cartons(int value)      { return of(value, Unit.CARTON); }

    public static Quantity zero(Unit unit) {
        return new Quantity(BigDecimal.ZERO, unit);
    }

    // ── Opérations (retournent un NOUVEAU Quantity) ───────────────

    public Quantity add(Quantity other) {
        assertSameUnit(other);
        return new Quantity(this.value.add(other.value), this.unit);
    }

    public Quantity subtract(Quantity other) {
        assertSameUnit(other);
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Soustraction impossible : stock négatif. Disponible: "
                            + this + ", Demandé: " + other
            );
        }
        return new Quantity(result, this.unit);
    }

    public Quantity multiply(int factor) {
        if (factor < 0) throw new IllegalArgumentException("Facteur négatif interdit");
        return new Quantity(this.value.multiply(BigDecimal.valueOf(factor)), this.unit);
    }

    // ── Comparaisons ──────────────────────────────────────────────

    public boolean isGreaterThan(Quantity other) {
        assertSameUnit(other);
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(Quantity other) {
        assertSameUnit(other);
        return this.value.compareTo(other.value) < 0;
    }

    public boolean isGreaterThanOrEqual(Quantity other) {
        assertSameUnit(other);
        return this.value.compareTo(other.value) >= 0;
    }

    public boolean isZero() {
        return this.value.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return this.value.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Vérifie si la quantité disponible couvre la quantité demandée.
     * Utilisé dans StockService avant chaque mouvement de sortie.
     */
    public boolean canFulfill(Quantity requested) {
        assertSameUnit(requested);
        return this.value.compareTo(requested.value) >= 0;
    }

    /**
     * Calcule l'écart entre stock théorique et réel (pour inventaire).
     * Valeur positive = excédent. Valeur négative = manque.
     */
    public BigDecimal differenceWith(Quantity actual) {
        assertSameUnit(actual);
        return actual.value.subtract(this.value);
    }

    // ── Getters ───────────────────────────────────────────────────

    public BigDecimal getValue() { return value; }
    public Unit getUnit()        { return unit; }
    public int getIntValue()     { return value.intValueExact(); }

    // ── Validation interne ────────────────────────────────────────

    private void assertSameUnit(Quantity other) {
        if (this.unit != other.unit) {
            throw new IllegalArgumentException(
                    "Opération impossible entre unités différentes : "
                            + this.unit.getSymbol() + " et " + other.unit.getSymbol()
            );
        }
    }

    // ── Equals / HashCode — basés sur la VALEUR ──────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity other)) return false;
        return Objects.equals(value, other.value) &&
                unit == other.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        // Ex: "150 pcs" ou "2.500 kg"
        return value.toPlainString() + " " + unit.getSymbol();
    }
}
