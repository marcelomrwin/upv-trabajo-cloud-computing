package es.upv.posgrado.api.rest;

import es.upv.posgrado.api.exceptions.HotNewsNotExistsException;
import es.upv.posgrado.api.exceptions.JobExistsException;
import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.api.service.ApiService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {

    @Inject
    ApiService apiService;

    @Path("/hotnews")
    @GET
    public Response getHotNews() {
        return Response.ok(HotNews.listAll()).build();
    }

    @POST
    @Path("/jobs")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendJob(@FormParam("id") Long id) {
        try {
            Job job = apiService.createJob(id);
            return Response.status(Response.Status.CREATED).entity(job).build();
        } catch (JobExistsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(String.format("Job %s was already submitted with status %s", e.getJobId(), e.getJobStatus())).build();
        } catch (HotNewsNotExistsException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(String.format("Hot News %s does not exists", id)).build();
        }
    }

}
