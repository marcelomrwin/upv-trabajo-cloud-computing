package es.upv.posgrado.injector.rest;

import es.upv.posgrado.injector.model.News;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsEndpoint {
    @GET
    @Path("/id/{id}")
    public News getNewsById(@PathParam Long id) {
        News entity = News.findById(id);
        if (Objects.isNull(entity)) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return entity;
    }
}
