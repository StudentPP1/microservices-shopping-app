package com.test.microservices.product.dto;

public record InventoryProduct(
        String skuCode,
        Integer quantity
) {
}
