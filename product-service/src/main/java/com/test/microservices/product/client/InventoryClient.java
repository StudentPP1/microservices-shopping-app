package com.test.microservices.product.client;

import com.test.microservices.product.dto.InventoryProduct;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

// ! interface of http calls to inventory-service
public interface InventoryClient {
    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @PostExchange("/api/inventory/add")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    void addProductToInventory(@RequestBody InventoryProduct product);

    // ! return false => inventory service not working
    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable throwable) {
        log.info("Cannot get inventory for skuCode {}, failure reason: {}", skuCode, throwable.getMessage());
        return false;
    }
}
