import UserService from "./keycloak/UserService";

export default function AuthorizedElement({roles, children})  {

    const isAuthorized = () => {
        if ( UserService && roles) {
            return  UserService.hasRole(roles);
        }
        return false;
    }
    return isAuthorized() && children;
};