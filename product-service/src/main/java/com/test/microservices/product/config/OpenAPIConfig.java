package com.test.microservices.product.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ! change html of swagger ui
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI productServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service")
                        .description("Product Service API")
                        .version("1.0")
                        .license(new License().name("Apache 2.0"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Product Service documentation")
                        .url("https://product-service.com/docs") // must be real url
                );
    }
}
