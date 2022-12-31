import axios from "axios";

import keycloak from "../Keycloak";

const API_ENDPOINT = window._env_.API_ENDPOINT;

var axiosInstance = axios.create({
    baseURL: API_ENDPOINT,
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