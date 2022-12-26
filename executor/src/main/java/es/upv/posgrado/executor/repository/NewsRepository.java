package es.upv.posgrado.executor.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import es.upv.posgrado.common.model.NewsDTO;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
@Slf4j
public class NewsRepository {

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String jobDatabaseName;

    public void saveNews(NewsDTO newsDTO) {
        if (newsDTO.getGeneratedAt() == null) newsDTO.setGeneratedAt(LocalDateTime.now());
        getNewsCollection().insertOne(newsDTO);
    }

    public NewsDTO findById(Long id) {
        Bson filter = eq("id", id);
        FindIterable<NewsDTO> iterable = getNewsCollection().find(filter, NewsDTO.class);
        if (iterable.iterator().hasNext()) return iterable.first();
        return null;
    }

    private MongoCollection<NewsDTO> getNewsCollection() {
        return mongoClient.getDatabase(jobDatabaseName).getCollection("news", NewsDTO.class);
    }

}
