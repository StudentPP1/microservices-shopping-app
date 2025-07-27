package com.test.microservices.product.service;

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

    private final ProductRepository productRepository;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        product = productRepository.save(product);
        log.info("Product {} has been created", product);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(
                p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice())
        ).toList();
    }
}
