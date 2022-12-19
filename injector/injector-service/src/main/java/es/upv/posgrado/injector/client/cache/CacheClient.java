package es.upv.posgrado.injector.client.cache;

import es.upv.posgrado.common.model.NewsDTO;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class CacheClient {

    private ValueCommands<String, NewsDTO> newsValueCommands;

    public CacheClient(RedisDataSource ds, ReactiveRedisDataSource reactiveDs){
        newsValueCommands = ds.value(NewsDTO.class);
    }

   public void set(String key,NewsDTO newsDTO){
        newsValueCommands.set(key,newsDTO,new SetArgs().ex(Duration.ofHours(1)));
    }

    public NewsDTO get(String key){
        return newsValueCommands.get(key);
    }
}
