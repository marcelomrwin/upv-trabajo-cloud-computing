import { useKeycloak } from "@react-keycloak/web";

const PrivateRoute = ({ children }) => {
    const { keycloak } = useKeycloak();

    const isLoggedIn = keycloak.authenticated;

    return isLoggedIn ? children : (<div className='my-4'>You must be logged and have permission to see this page</div>);
};

export default PrivateRoute;