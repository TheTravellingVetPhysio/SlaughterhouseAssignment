package org.example;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrationstest: starter en AEGTE Spring Boot-kontekst med en AEGTE
 * H2-database (seedet fra data.sql) og en AEGTE gRPC-server -- blot
 * "in-process" i stedet for over et rigtigt TCP-stik, saa testen ikke
 * kraever en fri netvaerksport og koerer hurtigt og isoleret.
 *
 * Dette beviser hele kaeden virker: proto-kontrakt -> service ->
 * repository -> JPQL -> H2, ikke kun servicens egen mapningslogik
 * (det daekker unit-testen i TraceabilityGrpcServiceTest allerede).
 *
 * Testdataene er dem fra data.sql (se den fil for det fulde scenarie):
 *   - Produkt 1 er pakket fra en tray, som BAADE DK-001 og DK-002 har
 *     bidraget dele til.
 *   - Produkt 2 er pakket fra en tray, som kun DK-001 har bidraget til.
 *   - Produkt 3 er pakket fra en tray, som kun DK-003 har bidraget til.
 */
@SpringBootTest
@ActiveProfiles("test")
class TraceabilityGrpcServiceIntegrationTest {

    @GrpcClient("inProcess")
    private TraceabilityServiceGrpc.TraceabilityServiceBlockingStub stub;

    @Test
    void getAnimalsForProduct_product1_returnsBothContributingAnimals() {
        AnimalListResponse response = stub.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(1).build());

        assertThat(response.getAnimalsList())
                .extracting(AnimalInfo::getRegistrationNumber)
                .containsExactlyInAnyOrder("DK-001", "DK-002");
    }

    @Test
    void getAnimalsForProduct_product2_returnsOnlyDk001() {
        AnimalListResponse response = stub.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(2).build());

        assertThat(response.getAnimalsList())
                .extracting(AnimalInfo::getRegistrationNumber)
                .containsExactly("DK-001");
    }

    @Test
    void getAnimalsForProduct_unknownProductId_returnsEmptyList() {
        AnimalListResponse response = stub.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(9999).build());

        assertThat(response.getAnimalsList()).isEmpty();
    }

    @Test
    void getProductsForAnimal_dk001_isInBothProduct1And2() {
        ProductListResponse response = stub.getProductsForAnimal(
                AnimalRequest.newBuilder().setRegistrationNumber("DK-001").build());

        assertThat(response.getProductsList())
                .extracting(ProductInfo::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void getProductsForAnimal_dk003_isOnlyInProduct3() {
        ProductListResponse response = stub.getProductsForAnimal(
                AnimalRequest.newBuilder().setRegistrationNumber("DK-003").build());

        assertThat(response.getProductsList())
                .extracting(ProductInfo::getId)
                .containsExactly(3L);
    }

    @Test
    void getProductsForAnimal_unknownRegistrationNumber_returnsEmptyList() {
        ProductListResponse response = stub.getProductsForAnimal(
                AnimalRequest.newBuilder().setRegistrationNumber("DOES-NOT-EXIST").build());

        assertThat(response.getProductsList()).isEmpty();
    }
}
