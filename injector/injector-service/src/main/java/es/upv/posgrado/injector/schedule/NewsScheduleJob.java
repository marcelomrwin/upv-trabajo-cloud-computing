package es.upv.posgrado.injector.schedule;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.connectors.service.NewsService;
import es.upv.posgrado.injector.client.cache.CacheClient;
import es.upv.posgrado.injector.client.rss.RssFeedService;
import es.upv.posgrado.injector.client.rss.model.Item;
import es.upv.posgrado.injector.client.rss.model.Rss;
import es.upv.posgrado.injector.service.NewsProcessorService;
import io.quarkus.arc.All;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
@Singleton
public class NewsScheduleJob {

    @Inject
    @All
    List<NewsService> newsServiceList;

    @Inject
    NewsProcessorService processorService;

    @Inject
    RssFeedService rssFeedService;

    @Inject
    CacheClient cacheClient;

    int serviceClientIndex = 0;

    @Scheduled(cron = "{cron.expr}")
    void cronJobGetNews() {
        log.info("Starting cronJobGetNews");
        processNews();
    }

    public void rotateNewsServiceIndex() {
        rotateServiceIndex();
    }

    @Fallback(fallbackMethod = "fallback")
    public void processNews() {
        try {
            NewsService newsService = newsServiceList.get(serviceClientIndex);
            log.info("\n=====\nPROCESSING INSTANCE {} INDEX {}", newsService.getClass().getName(), serviceClientIndex);
            Set<NewsDTO> newsDTOSet = newsService.getNews();
            log.info("Processing {} news", newsDTOSet.size());
            processorService.saveNewsFromArticle(newsDTOSet);
        } catch (PersistenceException persistenceException) {
            log.error("Persistence Exception {}", persistenceException.getMessage());
        } catch (CircuitBreakerOpenException circuitBreakerOpenException) {
            log.error("Service {} unavailable!!", newsServiceList.get(serviceClientIndex).getClass().getName());
            rotateServiceIndex();
        } catch (RuntimeException rte) {
            log.error("Client Service Error\n{}", rte.getMessage());

            if (rte instanceof WebApplicationException | rte.getCause() instanceof WebApplicationException) {
                WebApplicationException wae = rte instanceof WebApplicationException ? (WebApplicationException) rte : (WebApplicationException) rte.getCause();
                switch (wae.getResponse().getStatus()) {
                    case 429:
                    case 426: {
                        log.info("Generating news from Qaurkus RSS Feed https://quarkus.io/feed.xml");
                        Set<NewsDTO> newsDTOSet = getNewsFromQuarkusFeed(); //contingence
                        processorService.saveNewsFromArticle(newsDTOSet);
                        break;
                    }
                    default:
                        throw wae;
                }
            }
        } catch (Exception e) {
            log.error("Receive a unexpected exception", e);
            throw e;
        }
    }

    private Set<NewsDTO> getNewsFromQuarkusFeed() {
        Set<NewsDTO> news = new HashSet<>();
        try {
            Rss rss = cacheClient.getRss();
            if (rss == null) {
                rss = rssFeedService.generateNewsDTOfromQuarkusFeed();
                cacheClient.addRss(rss);
            }

            long id = new Random().nextLong(1000, 10000);
            int index = new Random().nextInt(0, rss.getChannel().getItem().size());
            Item item = rss.getChannel().getItem().get(index);
            item.setTitle(item.getTitle() + " " + id);

            news.add(NewsDTO.builder()
                    .id(id)
                    .generatedAt(LocalDateTime.now())
                    .publishedAt(OffsetDateTime.parse(item.getPubDate(), DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z")).toLocalDateTime())
                    .title(item.getTitle())
                    .urlToImage("https://registry.quarkus.io/q/swagger-ui/logo.png")
                    .description(item.getDescription())
                    .build());
        } catch (Exception e) {
            log.error("Fail in get news from rss feed", e);
        }
        return news;
    }

    private void rotateServiceIndex() {
        log.warn("\n=====\nACTIVATING FAULT TOLERANCE. ACTUAL INDEX {} WITH INSTANCE OF TYPE {}\n=====", serviceClientIndex, newsServiceList.get(serviceClientIndex).getClass().getName());
        serviceClientIndex++;
        if (serviceClientIndex >= newsServiceList.size()) serviceClientIndex = 0;
        log.warn("\n=====\nNEW INDEX {} WITH INSTANCE {}\n=====", serviceClientIndex, newsServiceList.get(serviceClientIndex).getClass().getName());
    }

    private void fallback() {
        rotateServiceIndex();
        processNews();
    }
}
