import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "https://shop-app.com/auth/",   // важливо: з / у кінці
    realm: "spring-microservices-security-realm",
    clientId: "frontend-client",
});

export default keycloak;