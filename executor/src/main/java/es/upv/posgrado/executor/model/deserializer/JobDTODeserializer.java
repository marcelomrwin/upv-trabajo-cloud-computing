package es.upv.posgrado.executor.model.deserializer;


import es.upv.posgrado.common.model.Job;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class JobDTODeserializer extends ObjectMapperDeserializer<Job> {
    public JobDTODeserializer() {
        super(Job.class);
    }
}
