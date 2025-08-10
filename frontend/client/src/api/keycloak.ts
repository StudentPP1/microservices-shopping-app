import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "spring-microservices-security-realm",
  clientId: "frontend-client",
});

export default keycloak;