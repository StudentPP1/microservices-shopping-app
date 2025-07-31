package com.test.microservices.product.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.microservices.product.model.Product;
import com.test.microservices.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductJsonLoader {

    private final ProductRepository repository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadJsonData() throws IOException {
        if (repository.count() == 0) {
            InputStream input = getClass().getResourceAsStream("/products.json");
            List<Product> products = objectMapper.readValue(input, new TypeReference<>() {});
            repository.saveAll(products);
        }
    }
}
