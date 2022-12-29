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
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Instance;
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

    @Inject
    Instance<SecurityIdentity> identity;

    @Path("/hotnews")
    @GET
    @Authenticated
    @Operation(description = "Returns all news suitable for publication without pagination")
    public Response getHotNews() {
        return Response.ok(HotNews.list("status = ?1", Sort.ascending("id"), NewsStatus.RECENT)).build();
    }

    @Path("/hotnews/paged")
    @GET
    @Authenticated
    @Operation(description = "Returns all news suitable for publication with pagination")
    public Response getHotNewsPaged(@QueryParam("title") String title, @QueryParam("page") int page, @QueryParam("size") int size) {
        return Response.status(Response.Status.OK).entity(newsRepository.findAllPaged(title, page, size)).build();
    }

    @Path("/jobs/paged")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Return all jobs from repository using paging",
            description = "A list of jobs and its status"
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
    @RolesAllowed({"ADMIN"})
    public Response getJobsInfo(@QueryParam("id") Long id, @QueryParam("title") String title, @QueryParam("page") @NotNull @DefaultValue("0") int page, @QueryParam("size") @NotNull @DefaultValue("10") int size) {
        return Response.status(Response.Status.OK).entity(jobRepository.findAllPaged(id, title, page, size)).build();
    }

    @GET
    @Path("/jobs/myjobs")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Operation(description = "Returns jobs submitted by the authenticated user")
    @ActivateRequestContext
    public Response getJobsSubmittedBy(@QueryParam("id") Long id, @QueryParam("title") String title, @QueryParam("page") @NotNull @DefaultValue("0") int page, @QueryParam("size") @NotNull @DefaultValue("10") int size) {
        return Response.status(Response.Status.OK).entity(jobRepository.findSubmittedBy(identity.get().getPrincipal().getName(), id, title, page, size)).build();
    }

    @POST
    @Path("/jobs")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"ADMIN", "USER"})
    @Operation(description = "Submit the news for publication. Starts a job request.")
    @ActivateRequestContext
    public Response submitJob(@FormParam("id") Long id) {
        try {
            Job job = apiService.createJob(id, identity.get().getPrincipal().getName());
            return Response.status(Response.Status.CREATED).entity(job).build();
        } catch (JobExistsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(String.format("Job %s was already submitted with status %s", e.getJobId(), e.getJobStatus())).build();
        } catch (HotNewsNotExistsException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(String.format("Hot News %s does not exists", id)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("Unexpected Error for Job %s. %s", id, e.getMessage())).build();
        }
    }

    @GET
    @Path("/jobs/result")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"ADMIN", "USER"})
    @Operation(description = "Allows the download of the completed work. The published news")
    @ActivateRequestContext
    public Response downloadJobResult(@QueryParam("id") @NotNull Long id) {
        Job job = jobRepository.findByIdOptional(id).orElseThrow(() -> new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Job ID " + id + " not found").build()));

        SecurityIdentity securityIdentity = identity.get();
        if (!securityIdentity.hasRole("ADMIN")) {
            String userLogged = securityIdentity.getPrincipal().getName();
            if (!userLogged.equals(job.getSubmittedBy()))
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("User " + userLogged + " does not have access to the Job " + id).build());
        }

        try {
            Response.ResponseBuilder response = Response.ok(job.getResult());
            response.header("Content-Disposition", "attachment;filename=" + id + ".html");
            return response.build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("Fail during download job %s result", id)).build();
        }
    }

}
