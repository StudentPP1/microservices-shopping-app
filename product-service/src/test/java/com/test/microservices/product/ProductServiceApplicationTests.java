package com.test.microservices.product;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // ! avoid port conflict
class ProductServiceApplicationTests {

    @ServiceConnection // ! inject properties from application.properties for connection to db
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    // ! inject port from webEnvironment
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                    "name": "iPhone 15",
                    "description": "smartphone from Apple",
                    "price": 1000
                }
                """;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.is("iPhone 15"))
                .body("description", Matchers.is("smartphone from Apple"))
                .body("price", Matchers.is(1000));
    }
}
