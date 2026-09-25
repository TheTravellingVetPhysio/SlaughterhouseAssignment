package org.example.domain;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Et produkt pakket på station 3, f.eks. "kasse med 20 skinker" eller
 * "halvt dyr". Et produkt refererer til en eller flere trays, det er
 * pakket fra (se {@link ProductTray}).
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_type", nullable = false, length = 128)
    private String productType;

    @Column(name = "packed_at", nullable = false)
    private Instant packedAt;

    protected Product() {
        // JPA
    }

    public Product(String productType, Instant packedAt) {
        this.productType = productType;
        this.packedAt = packedAt;
    }

    public Long getId() {
        return id;
    }

    public String getProductType() {
        return productType;
    }

    public Instant getPackedAt() {
        return packedAt;
    }
}
