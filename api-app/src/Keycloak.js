import Keycloak from "keycloak-js";

const keycloakConfig = {
    url: "http://localhost:8180",
    realm: "cc",
    clientId: "api-app",
}
const keycloak = new Keycloak(keycloakConfig);
export default keycloak;