package com.test.microservices.inventory.dto;

public record InventoryProduct(
        String skuCode,
        Integer quantity
) {
}
