package es.upv.posgrado.injector.client.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.RecentNewsDTO;
import es.upv.posgrado.injector.model.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@Slf4j
public class KafkaClient {
    @ConfigProperty(name = "app.kafka.topic.name", defaultValue = "recent-news")
    String topicName;
    @Inject
    Producer<String, String> producer;
    @Inject
    ObjectMapper objectMapper;

    @Inject
    ManagedExecutor managedExecutor;

    public CompletableFuture<Void> notifyRecentNews(News newsEntity) {
        return CompletableFuture.runAsync(new NotificationExecutor(producer, newsEntity, objectMapper, topicName), managedExecutor);
    }

    private static class NotificationExecutor implements Runnable {
        private final Producer<String, String> producer;
        private final News newsEntity;
        private final ObjectMapper objectMapper;
        private final String topicName;

        private NotificationExecutor(Producer<String, String> producer, News newsEntity, ObjectMapper objectMapper, String topicName) {
            this.producer = producer;
            this.newsEntity = newsEntity;
            this.objectMapper = objectMapper;
            this.topicName = topicName;
        }

        @Override
        public void run() {
            try {
                RecentNewsDTO recentNewsDTO = RecentNewsDTO.builder().id(newsEntity.id).title(newsEntity.getTitle()).publishedAt(newsEntity.getPublishedAt()).thumbnail(newsEntity.getThumbnail()).build();
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, String.valueOf(recentNewsDTO.getId()), objectMapper.writeValueAsString(recentNewsDTO));
                producer.send(record, (recordMetadata, e) -> {
                    if (e == null) {
                        // the record was successfully sent
                        log.debug("Received new metadata. \n" + "Topic:" + recordMetadata.topic() + "\n" + "Key:" + record.key() + "\n" + "Partition: " + recordMetadata.partition() + "\n" + "Offset: " + recordMetadata.offset() + "\n" + "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        log.error("Error while producing message to kafka cluster", e);
                        //TODO dead letter queue?? retry??
                    }
                });
            } catch (Exception e) {
                log.error("Error in " + getClass().getName(), e);
            } finally {
                producer.flush();
            }
        }
    }
}
