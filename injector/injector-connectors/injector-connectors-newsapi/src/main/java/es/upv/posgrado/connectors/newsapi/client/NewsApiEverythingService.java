package es.upv.posgrado.connectors.newsapi.client;


import es.upv.posgrado.connectors.newsapi.model.ArticleResponse;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/everything")
@RegisterRestClient(configKey = "newsapi")
public interface NewsApiEverythingService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArticleResponse getEverythingNews(@QueryParam @NotNull String apiKey, @QueryParam @NotNull String q, @QueryParam @DefaultValue("en") String language, @QueryParam @DefaultValue("2022-12-01") String from, @QueryParam @DefaultValue("publishedAt") String sortBy, @QueryParam @DefaultValue("5") String pageSize, @QueryParam @DefaultValue("1") String page);

}
