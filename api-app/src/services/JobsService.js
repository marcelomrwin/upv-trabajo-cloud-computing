import http from "../http-common";
import qs from 'qs'

const getJobs = (params) =>{
    return http.get("/jobs/paged", {params});
}

const submitJob = (id) =>{
    return http.post("/jobs",qs.stringify({id: id}),{headers:{'content-type': 'application/x-www-form-urlencoded;charset=utf-8'}})
}

const JobsService = {
    getJobs,
    submitJob
};

export default JobsService;