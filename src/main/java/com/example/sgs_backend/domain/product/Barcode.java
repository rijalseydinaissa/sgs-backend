package com.example.sgs_backend.domain.product;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * ✅ extends BaseEntity
 * Gère les codes-barres générés et imprimés par le système.
 */
@Entity
@Table(name = "barcodes", indexes = {
    @Index(name = "idx_barcodes_code",       columnList = "code"),
    @Index(name = "idx_barcodes_product_id", columnList = "product_id")
})
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Barcode extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;            // Valeur du code-barres

    @Column(name = "barcode_type", nullable = false, length = 20)
    private String barcodeType;     // EAN13, QR_CODE, CODE128, UPC

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "label_printed", nullable = false)
    private boolean labelPrinted = false;

    @Column(name = "print_count", nullable = false)
    private int printCount = 0;

    public void recordPrint() {
        this.labelPrinted = true;
        this.printCount++;
    }
}
