package com.test.microservices.inventory.service;

import com.test.microservices.inventory.dto.InventoryProduct;
import com.test.microservices.inventory.model.Inventory;
import com.test.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        // ! find inventory for given skuCode where quantity >= 0
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    @Transactional
    public void addProductToInventory(InventoryProduct product) {
        String skuCode = product.skuCode();
        Integer quantity = product.quantity();

        inventoryRepository.findBySkuCode(skuCode)
                .ifPresentOrElse(inventory ->
                        {
                            inventory.setQuantity(quantity);
                            inventoryRepository.save(inventory);
                        },
                        () -> inventoryRepository.save(Inventory.builder()
                                .skuCode(skuCode)
                                .quantity(quantity)
                                .build())
                );
    }
}