package es.upv.posgrado.executor.repository.sources;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.client.cache.CacheClient;
import io.quarkus.arc.Priority;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Priority(3)
public class CacheNewsSource extends AbstractNewsSource {

    @Inject
    CacheClient cacheClient;

    @Override
    protected NewsDTO doFind(Long id) {
        NewsDTO newsDTO = cacheClient.get(String.valueOf(id));
        return newsDTO;
    }
}
