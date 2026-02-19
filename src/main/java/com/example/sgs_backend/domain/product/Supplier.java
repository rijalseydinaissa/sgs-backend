package com.example.sgs_backend.domain.product;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.valueobject.Address;
import jakarta.persistence.*;
import lombok.*;

/**
 * ✅ extends BaseEntity
 * Utilise les Value Objects Address et ContactInfo du Sprint 1.
 */
@Entity
@Table(name = "suppliers", indexes = {
    @Index(name = "idx_suppliers_code",  columnList = "code"),
    @Index(name = "idx_suppliers_email", columnList = "email")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Supplier extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;        // ex: "FRNR-001"

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "tax_number", length = 50)
    private String taxNumber;   // Numéro NINEA / NIF

    // ── Value Objects intégrés (Sprint 1) ─────────────────────────
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street",     column = @Column(name = "address_street")),
        @AttributeOverride(name = "city",       column = @Column(name = "address_city")),
        @AttributeOverride(name = "region",     column = @Column(name = "address_region")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
        @AttributeOverride(name = "country",    column = @Column(name = "address_country"))
    })
    private Address address;

    @Column(name = "payment_terms_days")
    private Integer paymentTermsDays = 30;  // Délai de paiement standard

    @Column(name = "average_rating")
    private Double averageRating;           // Note moyenne (qualité, délais, prix)

    @Column(nullable = false)
    private boolean active = true;

    // ── Logique métier ────────────────────────────────────────────

    public boolean isReliable() {
        return averageRating != null && averageRating >= 3.5;
    }
}
