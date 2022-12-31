import UserService from "../security/keycloak/UserService";

const PrivateRoute = ({ children }) => {
    return UserService.isLoggedIn() ? children : (<div className='my-4'>You must be logged and have permission to see this page</div>);
};

export default PrivateRoute;