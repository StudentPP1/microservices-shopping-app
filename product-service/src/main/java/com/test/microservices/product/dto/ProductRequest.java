package com.test.microservices.product.dto;

import java.math.BigDecimal;

public record ProductRequest(
        String skuCode,
        Integer quantity,
        String name,
        String description,
        BigDecimal price
) { }
