package org.example;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.domain.Animal;
import org.example.domain.Product;
import org.example.grpc.*;
import org.example.repository.AnimalRepository;
import org.example.repository.ProductRepository;

/**
 * TRIN 3: rigtig implementering, der slaar op i databasen via
 * AnimalRepository og ProductRepository i stedet for at returnere
 * hardcodede data.
 */
@GrpcService
public class TraceabilityGrpcService extends TraceabilityServiceGrpc.TraceabilityServiceImplBase {

    private final AnimalRepository animalRepository;
    private final ProductRepository productRepository;

    // Spring injicerer automatisk de to repositories her (constructor injection).
    public TraceabilityGrpcService(AnimalRepository animalRepository, ProductRepository productRepository) {
        this.animalRepository = animalRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void getAnimalsForProduct(ProductRequest request, StreamObserver<AnimalListResponse> responseObserver) {
        var animals = animalRepository.findAnimalsContributingToProduct(request.getProductId());

        AnimalListResponse.Builder responseBuilder = AnimalListResponse.newBuilder();
        for (Animal animal : animals) {
            responseBuilder.addAnimals(toAnimalInfo(animal));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProductsForAnimal(AnimalRequest request, StreamObserver<ProductListResponse> responseObserver) {
        var products = productRepository.findProductsContainingAnimal(request.getRegistrationNumber());

        ProductListResponse.Builder responseBuilder = ProductListResponse.newBuilder();
        for (Product product : products) {
            responseBuilder.addProducts(toProductInfo(product));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private AnimalInfo toAnimalInfo(Animal animal) {
        return AnimalInfo.newBuilder()
                .setId(animal.getId())
                .setRegistrationNumber(animal.getRegistrationNumber())
                .setSpecies(animal.getSpecies())
                .setWeightKg(animal.getWeightKg())
                .setArrivedAt(animal.getArrivedAt().toString())
                .build();
    }

    private ProductInfo toProductInfo(Product product) {
        return ProductInfo.newBuilder()
                .setId(product.getId())
                .setProductType(product.getProductType())
                .setPackedAt(product.getPackedAt().toString())
                .build();
    }
}