package es.upv.posgrado.injector.client.rss;

import es.upv.posgrado.injector.client.rss.model.Rss;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class RssFeedService {
    @Inject
    @RestClient
    QuarkusFeedInterface quarkusFeedInterface;

    public Rss generateNewsDTOfromQuarkusFeed() {
        return quarkusFeedInterface.readRssFeed();
    }
}
