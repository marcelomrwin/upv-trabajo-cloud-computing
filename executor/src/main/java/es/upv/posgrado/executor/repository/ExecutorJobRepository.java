package es.upv.posgrado.executor.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import es.upv.posgrado.common.model.Job;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExecutorJobRepository {

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String jobDatabaseName;

    public void saveJob(Job job) {
        getJobCollection().insertOne(job);
    }

    private MongoCollection<Job> getJobCollection() {
        return mongoClient.getDatabase(jobDatabaseName).getCollection("job", Job.class);
    }
}
