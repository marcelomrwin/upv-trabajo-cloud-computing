import Keycloak from "keycloak-js";

const KEYCLOAK_BASE_URL = window._env_.KEYCLOAK_BASE_URL;

const keycloakConfig = {
    url: KEYCLOAK_BASE_URL,
    realm: "cc",
    clientId: "api-app",
}
const kc = new Keycloak(keycloakConfig);

export default kc;