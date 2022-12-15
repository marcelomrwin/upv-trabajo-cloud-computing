package es.upv.posgrado.injector.schedule;

import es.upv.posgrado.connectors.model.NewsDTO;
import es.upv.posgrado.connectors.service.NewsService;
import es.upv.posgrado.injector.service.NewsProcessorService;
import io.quarkus.arc.All;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

@Slf4j
@Singleton
public class NewsScheduleJob {

    @Inject
    @All
    List<NewsService> newsServiceList;

    @Inject
    NewsProcessorService processorService;

    int serviceClientIndex = 0;

    @Scheduled(cron = "{cron.expr}")
    void cronJobGetNews() {
        log.info("Starting cronJobGetNews");
        processNews();
    }

    @Fallback(fallbackMethod = "rotateServiceIndex")
    public void processNews() {
        try {
            NewsService newsService = newsServiceList.get(serviceClientIndex);
            log.info("Processing instance of " + newsService.getClass().getName());
            Set<NewsDTO> newsDTOSet = newsService.getNews();
            log.info("Processing {} news",newsDTOSet.size());
            processorService.saveNewsFromArticle(newsDTOSet);
        } catch (CircuitBreakerOpenException circuitBreakerOpenException) {
            log.error("Service unavailable!!");
        }catch (Exception e){
            log.error("Receive a unexpected exception",e);
        }
    }

    private void rotateServiceIndex() {
        log.warn("Activating Fault Tolerance");
        serviceClientIndex++;
        if (serviceClientIndex >= newsServiceList.size()) serviceClientIndex = 0;
        log.warn("Calling processNews Again");
        processNews();
    }
}
