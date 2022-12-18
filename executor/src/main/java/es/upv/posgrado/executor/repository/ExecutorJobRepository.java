package es.upv.posgrado.executor.repository;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import es.upv.posgrado.common.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class ExecutorJobRepository {

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String jobDatabaseName;

    public void saveJob(Job job) {
        getJobCollection().insertOne(job);
    }

    public void updateJob(Job job) {

        Document query = new Document().append(Job.ATTRIBUTE_TITLE, job.getTitle());
        Bson updates = Updates.combine(
                Updates.set(Job.ATTRIBUTE_RESULT, job.getResult()),
                Updates.set(Job.ATTRIBUTE_STATUS, job.getStatus()),
                Updates.set(Job.ATTRIBUTE_PROCESSED_AT, job.getProcessedAt()),
                Updates.set(Job.ATTRIBUTE_PROCESSED_BY, job.getProcessedBy())
        );
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            getJobCollection().updateOne(query, updates, options);
        } catch (MongoException e) {
            log.error("Fail update mongodb document {}", job.getId(), e);
            throw new RuntimeException(e);
        }

    }

    private MongoCollection<Job> getJobCollection() {
        return mongoClient.getDatabase(jobDatabaseName).getCollection("jobs", Job.class);
    }
}
