package org.example;

import io.grpc.stub.StreamObserver;
import org.example.domain.Animal;
import org.example.domain.Product;
import org.example.grpc.*;
import org.example.repository.AnimalRepository;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Ren unit-test: repositories er mockede, saa testen kun verificerer
 * TraceabilityGrpcService's egen logik (mapning fra JPA-entitet til
 * gRPC-besked). Ingen database, ingen netvaerk, koerer paa millisekunder.
 */
@ExtendWith(MockitoExtension.class)
class TraceabilityGrpcServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StreamObserver<AnimalListResponse> animalResponseObserver;

    @Mock
    private StreamObserver<ProductListResponse> productResponseObserver;

    private TraceabilityGrpcService service;

    @BeforeEach
    void setUp() {
        service = new TraceabilityGrpcService(animalRepository, productRepository);
    }

    @Test
    void getAnimalsForProduct_mapsRepositoryResultToGrpcResponse() {
        Animal animal = newAnimalWithId(1L, "DK-001", "Pig", 85.5, "2026-09-01T08:00:00Z");
        when(animalRepository.findAnimalsContributingToProduct(1L)).thenReturn(List.of(animal));

        service.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(1).build(),
                animalResponseObserver);

        ArgumentCaptor<AnimalListResponse> captor = ArgumentCaptor.forClass(AnimalListResponse.class);
        verify(animalResponseObserver).onNext(captor.capture());
        verify(animalResponseObserver).onCompleted();

        AnimalListResponse response = captor.getValue();
        assertThat(response.getAnimalsList()).hasSize(1);

        AnimalInfo info = response.getAnimals(0);
        assertThat(info.getId()).isEqualTo(1L);
        assertThat(info.getRegistrationNumber()).isEqualTo("DK-001");
        assertThat(info.getSpecies()).isEqualTo("Pig");
        assertThat(info.getWeightKg()).isEqualTo(85.5);
        assertThat(info.getArrivedAt()).isEqualTo("2026-09-01T08:00:00Z");
    }

    @Test
    void getAnimalsForProduct_noContributingAnimals_returnsEmptyListNotError() {
        when(animalRepository.findAnimalsContributingToProduct(999L)).thenReturn(List.of());

        service.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(999).build(),
                animalResponseObserver);

        ArgumentCaptor<AnimalListResponse> captor = ArgumentCaptor.forClass(AnimalListResponse.class);
        verify(animalResponseObserver).onNext(captor.capture());
        verify(animalResponseObserver).onCompleted();

        assertThat(captor.getValue().getAnimalsList()).isEmpty();
    }

    @Test
    void getProductsForAnimal_mapsRepositoryResultToGrpcResponse() {
        Product product = newProductWithId(1L, "Half animal", "2026-09-02T10:00:00Z");
        when(productRepository.findProductsContainingAnimal("DK-001")).thenReturn(List.of(product));

        service.getProductsForAnimal(
                AnimalRequest.newBuilder().setRegistrationNumber("DK-001").build(),
                productResponseObserver);

        ArgumentCaptor<ProductListResponse> captor = ArgumentCaptor.forClass(ProductListResponse.class);
        verify(productResponseObserver).onNext(captor.capture());
        verify(productResponseObserver).onCompleted();

        ProductListResponse response = captor.getValue();
        assertThat(response.getProductsList()).hasSize(1);

        ProductInfo info = response.getProducts(0);
        assertThat(info.getId()).isEqualTo(1L);
        assertThat(info.getProductType()).isEqualTo("Half animal");
        assertThat(info.getPackedAt()).isEqualTo("2026-09-02T10:00:00Z");
    }

    // --- Test-hjaelpere -----------------------------------------------
    // Animal/Product's id bliver normalt sat af JPA ved indsaettelse i
    // databasen, og entiteterne har bevidst ingen public setId() (for at
    // undgaa at nogen "opfinder" et id udenom databasen). Til testen saetter
    // vi derfor id'et via reflection, i stedet for at aabne op for en
    // setter der ikke skal bruges andre steder i produktionskoden.

    private Animal newAnimalWithId(long id, String regNumber, String species, double weightKg, String arrivedAt) {
        Animal animal = new Animal(regNumber, species, weightKg, Instant.parse(arrivedAt));
        setId(animal, id);
        return animal;
    }

    private Product newProductWithId(long id, String productType, String packedAt) {
        Product product = new Product(productType, Instant.parse(packedAt));
        setId(product, id);
        return product;
    }

    private void setId(Object entity, long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
