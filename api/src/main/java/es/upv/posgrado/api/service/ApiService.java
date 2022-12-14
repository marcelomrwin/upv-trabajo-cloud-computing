package es.upv.posgrado.api.service;

import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
@Slf4j
public class ApiService {

    @Inject
    Producer<String, Job> producer;

    @ConfigProperty(name = "app.kafka.topic.job.out.name", defaultValue = "job-requestf")
    String jobRequestTopicName;

    @Transactional
    public Job createJob(HotNews hotNews) {

        Job job = Job.builder().id(hotNews.getId())
                .title(hotNews.getTitle()).status(JobStatus.SUBMITTED)
                .requestedAt(LocalDateTime.now())
                .publishedAt(hotNews.getPublishedAt()).build();

        job.persist();

        try {
            ProducerRecord<String, Job> record = new ProducerRecord<>(jobRequestTopicName, String.valueOf(job.getId()), job);
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
                    log.error("Error while producing message to kafka cluster", e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.flush();
        }

        return job;
    }
}