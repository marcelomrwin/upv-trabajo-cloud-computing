package es.upv.posgrado.injector.schedule;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.connectors.service.NewsService;
import es.upv.posgrado.injector.service.NewsProcessorService;
import io.quarkus.arc.All;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
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

    public void rotateNewsServiceIndex(){
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
        } catch (javax.ws.rs.WebApplicationException wae) {
            log.error("Client Service Error\n{}", wae.getMessage());
            throw wae;
        } catch (Exception e) {
            log.error("Receive a unexpected exception", e);
//            if (!(e.getCause() instanceof WebApplicationException))
            throw e;
        }
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
