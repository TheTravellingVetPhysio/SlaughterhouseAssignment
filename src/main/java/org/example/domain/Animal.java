package org.example.domain;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Et dyr, registreret ved ankomst paa station 1 (vejning og registrering).
 */
@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Registreringsnummer tildelt ved ankomst. Bruges som ekstern noegle. */
    @Column(name = "registration_number", nullable = false, unique = true, length = 64)
    private String registrationNumber;

    @Column(name = "species", nullable = false, length = 64)
    private String species;

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "arrived_at", nullable = false)
    private Instant arrivedAt;

    protected Animal() {
        // JPA
    }

    public Animal(String registrationNumber, String species, double weightKg, Instant arrivedAt) {
        this.registrationNumber = registrationNumber;
        this.species = species;
        this.weightKg = weightKg;
        this.arrivedAt = arrivedAt;
    }

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getSpecies() {
        return species;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public Instant getArrivedAt() {
        return arrivedAt;
    }
}
