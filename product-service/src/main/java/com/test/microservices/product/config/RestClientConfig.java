package com.test.microservices.product.config;

import com.test.microservices.product.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    // ! if a lot of => use @ConfigurationProperties class
    @Value("${inventory.url}")
    private String inventoryServiceUrl;

    // ! realization of interface for calling inventory service
    @Bean
    public InventoryClient inventoryClient() {
        // ! define rest client with url
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .requestFactory(requestFactory())
                .build();
        // ! bind RestClient to InventoryClient interface
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

    // ! add time limiter setting to refused connection if wait a lot
    private ClientHttpRequestFactory requestFactory() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // max time of waiting for TCP connection
        factory.setReadTimeout(3000); // max time of waiting for response after sending request
        return factory;
    }
}