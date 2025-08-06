package com.test.microservices.product.service;

import com.test.microservices.product.client.InventoryClient;
import com.test.microservices.product.dto.InventoryProduct;
import com.test.microservices.product.dto.ProductRequest;
import com.test.microservices.product.dto.ProductResponse;
import com.test.microservices.product.model.Product;
import com.test.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final InventoryClient inventoryClient;
    private final ProductRepository productRepository;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .skuCode(productRequest.skuCode())
                .build();
        product = productRepository.save(product);
        inventoryClient.addProductToInventory(new InventoryProduct(productRequest.skuCode(), productRequest.quantity()));
        log.info("Product {} has been created", product);
        return mapToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSkuCode()
        );
    }
}
