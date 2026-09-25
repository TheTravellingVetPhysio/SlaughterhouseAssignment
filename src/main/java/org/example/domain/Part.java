package org.example.domain;

import jakarta.persistence.*;

/**
 * En enkelt dyredel, skåret ud på station 2, vejet, registreret med
 * reference til dyret den kommer fra, og lagt i en tray.
 *
 * Dette er nøgle-koblingen i sporbarhedskæden: Part binder et Animal
 * sammen med en Tray.
 */
@Entity
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tray_id", nullable = false)
    private Tray tray;

    @Column(name = "part_type", nullable = false, length = 64)
    private String partType;

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    protected Part() {
        // JPA
    }

    public Part(Animal animal, Tray tray, String partType, double weightKg) {
        this.animal = animal;
        this.tray = tray;
        this.partType = partType;
        this.weightKg = weightKg;
    }

    public Long getId() {
        return id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Tray getTray() {
        return tray;
    }

    public String getPartType() {
        return partType;
    }

    public double getWeightKg() {
        return weightKg;
    }
}
