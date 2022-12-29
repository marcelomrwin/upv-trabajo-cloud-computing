package es.upv.posgrado.executor.client.messaging;

import es.upv.posgrado.common.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class KafkaProducer {

    @Inject
    Producer<String, Job> producer;

    @ConfigProperty(name = "mp.messaging.outgoing.job-out.topic", defaultValue = "job-response")
    String jobResponseTopic;

    @ConfigProperty(name = "mp.messaging.outgoing.job-compensation.topic", defaultValue = "job-compensation")
    String jobCompensation;


    //FAULT TOLERANCE (NETWORKING)
    public void sendMessage(Job job) {
        try {
            ProducerRecord<String, Job> record = new ProducerRecord<>(jobResponseTopic,job.getId(), job);
            producer.send(record, (recordMetadata, e) -> {
                if (e == null) {
                    // the record was successfully sent
                    log.debug("Received new metadata. \n" +
                            "Topic:" + recordMetadata.topic() + "\n" +
                            "Key:" + record.key() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    log.error("Error while producing message to kafka cluster for job\nId:"+job.getId()+"\nTitle:"+job.getTitle()+"\nStatus:"+job.getStatus(), e);
                    //TODO dead letter queue?
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.flush();
        }
    }
}
