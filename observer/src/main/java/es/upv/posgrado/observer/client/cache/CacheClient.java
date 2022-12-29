package es.upv.posgrado.observer.client.cache;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.common.model.monitoring.MonitoringResponse;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class CacheClient {

    private ValueCommands<String, MonitoringResponse> newsValueCommands;

    public CacheClient(RedisDataSource ds, ReactiveRedisDataSource reactiveDs) {
        newsValueCommands = ds.value(MonitoringResponse.class);
    }

    public MonitoringResponse get(String key) {
        return newsValueCommands.get(key);
    }

    public void put(String key,MonitoringResponse value){
        newsValueCommands.set(key, value, new SetArgs().ex(Duration.ofHours(1)));
    }
}
