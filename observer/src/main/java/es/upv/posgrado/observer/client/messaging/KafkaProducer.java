package es.upv.posgrado.observer.client.messaging;

import es.upv.posgrado.common.model.monitoring.JobStatistics;
import es.upv.posgrado.common.model.monitoring.MonitoringResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@Slf4j
public class KafkaProducer {

    @Inject
    Producer<Long, MonitoringResponse> producer;

    private AtomicLong messageIdGenerator = new AtomicLong();

    @ConfigProperty(name = "mp.messaging.outgoing.job-stats.topic", defaultValue = "job-stats")
    String jobStatisticsResponse;

    //TODO FAULT TOLERANCE (NETWORKING)
    public void sendMessage(MonitoringResponse monitoringResponse) {
        try {
            log.info("Sending Job Statistics");
            ProducerRecord<Long, MonitoringResponse> record = new ProducerRecord<>(jobStatisticsResponse, messageIdGenerator.incrementAndGet(), monitoringResponse);
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
                    log.error("Error while producing message to kafka cluster for monitoringResponse\n"+monitoringResponse.getCurrentStatistics().getReportDate(), e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.flush();
        }
    }
}
