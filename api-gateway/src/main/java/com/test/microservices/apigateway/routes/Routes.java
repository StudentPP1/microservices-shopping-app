package com.test.microservices.apigateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.web.servlet.function.RequestPredicates.path;
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
                .POST("/api/product", http())
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute") // if request error we redirect to fallback
                        )
                )
                .before(uri("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
                .POST("/api/order", http())
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                "orderServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute") // if request error we redirect to fallback
                        )
                )
                .before(uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return route("inventory_service")
                .GET("/api/inventory", http())
                .POST("/api/inventory/add", http())
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                "inventoryServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute") // if request error we redirect to fallback
                        )
                )
                .before(uri("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return route("product_service_swagger")
                .route(path("/aggregate/product-service/v3/api-docs"), http())
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                "productSwaggerServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute") // if request error we redirect to fallback
                        )
                )
                .filter(setPath("/api-docs")) // ! /aggregate/product-service/v3/api-docs -> /api-docs
                .before(uri("http://localhost:8080")) // ! add prefix
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return route("order_service_swagger")
                .route(path("/aggregate/order-service/v3/api-docs"), http())
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                "orderSwaggerServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute") // if request error we redirect to fallback
                        )
                )
                .filter(setPath("/api-docs"))
                .before(uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
        return route("inventory_service_swagger")
                .route(path("/aggregate/inventory-service/v3/api-docs"), http())
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                                "inventorySwaggerServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute") // if request error we redirect to fallback
                        )
                )
                .filter(setPath("/api-docs"))
                .before(uri("http://localhost:8082"))
                .build();
    }

    // create get endpoint like in MVC
    // if request is failing we show this message
    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service unavailable, please try again later")
                ).build();
    }
}