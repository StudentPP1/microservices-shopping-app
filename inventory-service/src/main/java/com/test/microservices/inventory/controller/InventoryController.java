package com.test.microservices.inventory.controller;

import com.test.microservices.inventory.dto.InventoryProduct;
import com.test.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProductToInventory(@RequestBody InventoryProduct product) {
        inventoryService.addProductToInventory(product);
    }
}