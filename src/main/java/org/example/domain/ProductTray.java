package org.example.domain;

import jakarta.persistence.*;

/**
 * Kobler et produkt til en tray, det (helt eller delvist) er pakket fra.
 * Et produkt kan referere flere trays, og en tray kan indgå i flere produkter
 * (f.eks. hvis kun en del af trayen blev brugt til et produkt).
 *
 * Dette er den registrering, station 3 laver: "dette produkt indeholder
 * dele fra disse trays".
 */
@Entity
@Table(name = "product_tray")
public class ProductTray {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tray_id", nullable = false)
    private Tray tray;

    protected ProductTray() {
        // JPA
    }

    public ProductTray(Product product, Tray tray) {
        this.product = product;
        this.tray = tray;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Tray getTray() {
        return tray;
    }
}
