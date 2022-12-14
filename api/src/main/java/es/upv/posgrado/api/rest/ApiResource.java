package es.upv.posgrado.api.rest;

import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.api.service.ApiService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

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

        Optional<Job> optional = Job.findByIdOptional(id);
        if (optional.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST).entity("Job was already submitted with status " + optional.get().getStatus()).build();
        }

        return  HotNews.findByIdOptional(id)
                        .map(hotnews -> Response.status(Response.Status.CREATED).entity(apiService.createJob((HotNews) hotnews)).build())
                        .orElse(Response.status(Response.Status.NOT_FOUND).entity(String.format("Hot News %s does not exists",id)).build());

    }

}
