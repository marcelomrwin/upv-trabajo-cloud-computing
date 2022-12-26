import axios from "axios";

import keycloak from "../Keycloak";


var axiosInstance = axios.create({
    baseURL: "http://localhost:8083/api",
    headers: {
        "Content-type": "application/json",
    }
});

axiosInstance.interceptors.request.use((config) => {
    if (keycloak.token){
        config.headers["Authorization"] = 'Bearer '+ keycloak.token;
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