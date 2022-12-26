package es.upv.posgrado.executor.repository.sources;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.client.cache.CacheClient;
import es.upv.posgrado.executor.repository.NewsRepository;
import io.quarkus.arc.Priority;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Priority(2)
public class LocalDbNewsSource extends AbstractNewsSource {

    @Inject
    NewsRepository newsRepository;

    @Inject
    CacheClient cacheClient;

    @Override
    protected NewsDTO doFind(Long id) {
        NewsDTO dto = newsRepository.findById(id);
        if (dto != null) {
            cacheClient.put(String.valueOf(id), dto);
        }
        return dto;
    }
}
