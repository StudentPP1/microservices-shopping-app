package com.test.microservices.order.service;

import com.test.microservices.order.client.InventoryClient;
import com.test.microservices.order.dto.OrderRequest;
import com.test.microservices.order.event.OrderPlacedEvent;
import com.test.microservices.order.model.Order;
import com.test.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    // key & value => String, OrderPlacedEvent
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public void placeOrder(OrderRequest orderRequest) {
        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);
            // send the message to Kafka Topic: orderNumber & email
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(
                    order.getOrderNumber(),
                    orderRequest.userDetails().email(),
                    orderRequest.userDetails().firstName(),
                    orderRequest.userDetails().lastName()
            );
            log.info("Start -> send order placed event {} to topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End -> send order placed event {} to topic order-placed", order);
        } else {
            throw new RuntimeException("Product with skuCode " +  orderRequest.skuCode() + " is not in stock");
        }
    }
}
