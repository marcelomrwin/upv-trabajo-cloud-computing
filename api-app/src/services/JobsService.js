import http from "../helpers/http-common";
import qs from 'qs'

const getJobs = (params) =>{
    return http.get("/jobs/paged", {params});
}

const getJobsSubmitted = (params) =>{
    return http.get("/jobs/myjobs",{params});
}

const submitJob = (id) =>{
    return http.post("/jobs",qs.stringify({id: id}),{headers:{'content-type': 'application/x-www-form-urlencoded;charset=utf-8'}})
}

const downloadJobResult = (id) =>{
    return http.get("/jobs/result",{params: {id: id}}, {headers: {responseType: 'blob'}})
}

const JobsService = {
    getJobs,
    getJobsSubmitted,
    submitJob,
    downloadJobResult
};

export default JobsService;