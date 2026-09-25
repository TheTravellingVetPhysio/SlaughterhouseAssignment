package org.example.repository;

import org.example.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Alle produkter, som det givne dyr (potentielt) indgår i.
     *
     * Kæden er: Animal -> Part -> Tray -> ProductTray -> Product.
     * Dette er forespørgslen, en tilbagekaldelse bygger på: "find alle
     * produkter der kan indeholde dele af dette dyr".
     */
    @Query("""
            SELECT DISTINCT p FROM Product p
            JOIN ProductTray pt ON pt.product = p
            JOIN Part part ON part.tray = pt.tray
            WHERE part.animal.registrationNumber = :registrationNumber
            ORDER BY p.id
            """)
    List<Product> findProductsContainingAnimal(@Param("registrationNumber") String registrationNumber);
}