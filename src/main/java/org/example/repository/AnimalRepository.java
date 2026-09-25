package org.example.repository;

import org.example.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByRegistrationNumber(String registrationNumber);

    /**
     * Alle dyr, der (potentielt) har leveret dele til det givne produkt.
     *
     * Kæden er: Product -> ProductTray -> Tray -> Part -> Animal.
     * DISTINCT er vigtigt: et produkt kan bestå af flere trays med dele fra
     * det samme dyr, og en tray kan bidrage med flere dele fra samme dyr.
     */
    @Query("""
            SELECT DISTINCT a FROM Animal a
            JOIN Part p ON p.animal = a
            JOIN ProductTray pt ON pt.tray = p.tray
            WHERE pt.product.id = :productId
            ORDER BY a.registrationNumber
            """)
    List<Animal> findAnimalsContributingToProduct(@Param("productId") Long productId);
}
