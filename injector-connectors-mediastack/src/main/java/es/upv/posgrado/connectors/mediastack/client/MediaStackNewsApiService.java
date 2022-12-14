package es.upv.posgrado.connectors.mediastack.client;

import es.upv.posgrado.connectors.mediastack.model.MediaStackNewsResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/news")
@RegisterRestClient(configKey = "mediastack")
public interface MediaStackNewsApiService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    MediaStackNewsResponse get(@QueryParam String access_key, @QueryParam String languages, @QueryParam String keywords, @QueryParam String limit, @QueryParam String offset);
}
