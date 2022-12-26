package es.upv.posgrado.executor.repository.sources;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.repository.NewsSource;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class AbstractNewsSource implements NewsSource {

    @Override
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.5, delay = 5, delayUnit = ChronoUnit.SECONDS, successThreshold = 2)
    public NewsDTO findHotNews(Long id) {
        NewsDTO newsDTO = doFind(id);

        if (newsDTO != null)
            if (newsDTO.getGeneratedAt() == null)
                newsDTO.setGeneratedAt(LocalDateTime.now());

        return newsDTO;
    }

    protected abstract NewsDTO doFind(Long id);

}
