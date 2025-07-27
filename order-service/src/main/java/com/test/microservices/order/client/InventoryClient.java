package com.test.microservices.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// ! http call to inventory-service
@FeignClient(value = "inventory", url = "${inventory.url}")
public interface InventoryClient {

    @GetMapping(value = "/api/inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
