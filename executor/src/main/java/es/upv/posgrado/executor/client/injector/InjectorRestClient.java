package es.upv.posgrado.executor.client.injector;

import es.upv.posgrado.common.model.NewsDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/news")
@RegisterRestClient(configKey = "injector")
public interface InjectorRestClient {

    @GET
    @Path("/id/{id}")
    NewsDTO getNewsById(@PathParam Long id);
}
