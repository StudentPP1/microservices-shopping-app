package com.test.microservices.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtConfig {

    @Bean
    JwtDecoder jwtDecoder() {
        // 1) Ключі беремо з ВНУТРІШНЬОГО Keycloak (кластерний DNS)
        String jwksUri = "http://keycloak:8080/auth/realms/spring-microservices-security-realm/protocol/openid-connect/certs";
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwksUri).build();

        // 2) Строго перевіряємо, що токен виданий зовнішнім issuer (те, що у Keycloak KC_HOSTNAME)
        String expectedIssuer = "https://shop-app.com/auth/realms/spring-microservices-security-realm";
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(expectedIssuer);

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer /*, audienceValidator*/));
        return decoder;
    }
}

