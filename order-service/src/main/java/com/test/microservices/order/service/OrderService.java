package com.test.microservices.order.service;

import com.test.microservices.order.client.InventoryClient;
import com.test.microservices.order.dto.OrderRequest;
import com.test.microservices.order.model.Order;
import com.test.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product with skuCode " +  orderRequest.skuCode() + " is not in stock");
        }
    }
}
