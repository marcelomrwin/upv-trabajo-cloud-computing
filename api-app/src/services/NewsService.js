import http from "../helpers/http-common";

const getNews = (params) =>{
    return http.get("/hotnews/paged", {params});
}

const NewsService = {
    getNews
};

export default NewsService;