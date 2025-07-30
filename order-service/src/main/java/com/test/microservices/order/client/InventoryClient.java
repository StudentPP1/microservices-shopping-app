package com.test.microservices.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

// ! interface of http calls to inventory-service
public interface InventoryClient {
    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @GetExchange("/api/inventory")
    // ! name should match with properties => inventory
    // ! handle error by logging in fallbackMethod
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory") // ! setup retry
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    // ! return false => inventory service not working
    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable throwable) {
        log.info("Cannot get inventory for skuCode {}, failure reason: {}", skuCode, throwable.getMessage());
        return false;
    }
}
