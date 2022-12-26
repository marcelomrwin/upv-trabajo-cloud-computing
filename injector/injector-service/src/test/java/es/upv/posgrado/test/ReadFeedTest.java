package es.upv.posgrado.test;

import es.upv.posgrado.injector.client.rss.RssFeedService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class ReadFeedTest {
    @Inject
    RssFeedService rssFeedService;

    @Test
    public void read(){
        rssFeedService.generateNewsDTOfromQuarkusFeed();
    }
}
