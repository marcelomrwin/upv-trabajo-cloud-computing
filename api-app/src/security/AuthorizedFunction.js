import UserService from "./keycloak/UserService";

const AuthorizedFunction = (roles) =>{

    const isAuthorized = () =>{
        if (UserService && roles){
            return UserService.hasRole(roles);
        }
        return false;
    }

    return isAuthorized();
};

export default AuthorizedFunction;