package es.upv.posgrado.executor.repository.sources;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.client.cache.CacheClient;
import es.upv.posgrado.executor.client.injector.InjectorRestClient;
import io.quarkus.arc.Priority;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Priority(1)
public class InjectorNewsSource extends AbstractNewsSource {
    @Inject
    @RestClient
    InjectorRestClient injectorRestClient;

    @Inject
    CacheClient cacheClient;

    @Override
    protected NewsDTO doFind(Long id) {
        NewsDTO dto = injectorRestClient.getNewsById(id);

        if (dto != null) {
            cacheClient.put(String.valueOf(id), dto);
        }

        return dto;
    }
}
