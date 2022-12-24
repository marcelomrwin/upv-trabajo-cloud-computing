package es.upv.posgrado.api.rest;

import es.upv.posgrado.api.exceptions.HotNewsNotExistsException;
import es.upv.posgrado.api.exceptions.JobExistsException;
import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.api.repository.JobRepository;
import es.upv.posgrado.api.repository.NewsRepository;
import es.upv.posgrado.api.service.ApiService;
import es.upv.posgrado.common.model.NewsStatus;
import io.quarkus.panache.common.Sort;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {

    @Inject
    ApiService apiService;

    @Inject
    NewsRepository newsRepository;
    @Inject
    JobRepository jobRepository;

    @Path("/hotnews")
    @GET
    public Response getHotNews() {
        return Response.ok(HotNews.list("status = ?1", Sort.ascending("id"), NewsStatus.RECENT)).build();
    }

    @Path("/hotnews/paged")
    @GET
    public Response getHotNewsPaged(@QueryParam("title") String title, @QueryParam("page") int page, @QueryParam("size") int size) {
        return Response.status(Response.Status.OK).entity(newsRepository.findAllPaged(title, page, size)).build();
    }

    @Path("/jobs/paged")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Return jobs from repository",
            description = "A list of jobs and his status"
    )
    @APIResponses(
            {
                    @APIResponse(responseCode = "200", description = "OK"),
                    @APIResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @Parameters({
            @Parameter(name = "title", description = "Title to search", required = false)
    })
    public Response getJobs(@QueryParam("title") String title, @QueryParam("page") @NotNull @DefaultValue("0") int page, @QueryParam("size") @NotNull @DefaultValue("5") int size) {
        return Response.status(Response.Status.OK).entity(jobRepository.findAllPaged(title, page, size)).build();
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
