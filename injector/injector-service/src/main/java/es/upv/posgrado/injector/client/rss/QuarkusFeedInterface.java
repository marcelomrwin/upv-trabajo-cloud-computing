package es.upv.posgrado.injector.client.rss;

import es.upv.posgrado.injector.client.rss.model.Rss;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/feed.xml")
@RegisterRestClient(configKey = "quarkus.io")
public interface QuarkusFeedInterface {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    Rss readRssFeed();
}
