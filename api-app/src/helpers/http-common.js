import axios from "axios";
import UserService from "../security/keycloak/UserService";


const API_ENDPOINT = window._env_.API_ENDPOINT;

var axiosInstance = axios.create({
    baseURL: API_ENDPOINT,
    headers: {
        "Content-type": "application/json",
    }
});

axiosInstance.interceptors.request.use((config) => {
    if (UserService.isLoggedIn() && UserService.getToken()){
        config.headers["Authorization"] = 'Bearer '+ UserService.getToken();
    }
    return config;
},(error) =>{
    return Promise.reject(error);
})

axiosInstance.interceptors.response.use((response) => {
    return response;
}, function (error) {
    return Promise.reject(error);
});

export default axiosInstance;