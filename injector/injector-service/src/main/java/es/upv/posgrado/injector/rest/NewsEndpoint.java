package es.upv.posgrado.injector.rest;

import es.upv.posgrado.injector.model.News;
import es.upv.posgrado.injector.schedule.NewsScheduleJob;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsEndpoint {
    @Inject
    NewsScheduleJob jobServices;

    @GET
    @Path("/id/{id}")
    public News getNewsById(@PathParam Long id) {
        News entity = News.findById(id);
        if (Objects.isNull(entity)) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return entity;
    }

    @PUT
    @Path("/rotate")
    public void rotateNewsSource(){
        jobServices.rotateNewsServiceIndex();
    }
}
