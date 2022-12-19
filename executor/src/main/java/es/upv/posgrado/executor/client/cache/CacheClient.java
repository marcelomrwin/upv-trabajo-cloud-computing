package es.upv.posgrado.executor.client.cache;

import es.upv.posgrado.common.model.NewsDTO;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CacheClient {

    private ValueCommands<String, NewsDTO> newsValueCommands;

    public CacheClient(RedisDataSource ds, ReactiveRedisDataSource reactiveDs) {
        newsValueCommands = ds.value(NewsDTO.class);
    }

    public NewsDTO get(String key) {
        return newsValueCommands.get(key);
    }
}
