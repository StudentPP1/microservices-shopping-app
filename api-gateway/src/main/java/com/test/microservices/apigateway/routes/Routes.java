package com.test.microservices.apigateway.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class Routes {
    // define all routes

    // function style of definite endpoints
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        // http() — функція-обробник, що проксуює запит на сервіс через Spring Cloud Gateway
        return route("product_service")
                .GET("/api/product", http())
                .before(uri("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
                .POST("/api/order", http())
                .before(uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return route("inventory_service")
                .GET("/api/inventory", http())
                .before(uri("http://localhost:8082"))
                .build();
    }
}

