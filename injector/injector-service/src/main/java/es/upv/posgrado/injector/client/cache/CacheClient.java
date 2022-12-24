package es.upv.posgrado.injector.client.cache;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.injector.client.rss.model.Rss;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class CacheClient {

    private ValueCommands<String, NewsDTO> newsValueCommands;
    private ValueCommands<String, Rss> rssValueCommands;

    public CacheClient(RedisDataSource ds, ReactiveRedisDataSource reactiveDs){
        newsValueCommands = ds.value(NewsDTO.class);
        rssValueCommands = ds.value(Rss.class);
    }

   public void set(String key,NewsDTO newsDTO){
        newsValueCommands.set(key,newsDTO,new SetArgs().ex(Duration.ofHours(1)));
    }

    public NewsDTO get(String key){
        return newsValueCommands.get(key);
    }

    public void addRss(Rss rss){
        rssValueCommands.set(rss.getClass().getName(),rss,new SetArgs().ex(Duration.ofHours(1)));
    }

    public Rss getRss(){
        return rssValueCommands.get(Rss.class.getName());
    }
}
