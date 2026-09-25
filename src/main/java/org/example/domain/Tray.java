package org.example.domain;

import jakarta.persistence.*;

/**
 * En bakke (tray) fra station 2. Indeholder kun én type dyredel, men kan
 * indeholde dele fra flere forskellige dyr, op til bakkens maksimale vægt.
 */
@Entity
@Table(name = "tray")
public class Tray {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_type", nullable = false, length = 64)
    private String partType;

    @Column(name = "max_weight_kg", nullable = false)
    private double maxWeightKg;

    protected Tray() {
        // JPA
    }

    public Tray(String partType, double maxWeightKg) {
        this.partType = partType;
        this.maxWeightKg = maxWeightKg;
    }

    public Long getId() {
        return id;
    }

    public String getPartType() {
        return partType;
    }

    public double getMaxWeightKg() {
        return maxWeightKg;
    }
}
