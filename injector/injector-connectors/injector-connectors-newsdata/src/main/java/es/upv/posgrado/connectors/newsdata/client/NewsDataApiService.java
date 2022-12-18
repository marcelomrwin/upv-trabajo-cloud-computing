package es.upv.posgrado.connectors.newsdata.client;

import es.upv.posgrado.connectors.newsdata.model.NewsDataResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/news")
@RegisterRestClient(configKey = "newsdata")
public interface NewsDataApiService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    NewsDataResponse get(@QueryParam String apikey,@QueryParam String language);
}
