package es.upv.posgrado.api.model.deserializer;



import es.upv.posgrado.api.model.Job;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class JobDeserializer extends ObjectMapperDeserializer<Job> {
    public JobDeserializer() {
        super(Job.class);
    }
}
