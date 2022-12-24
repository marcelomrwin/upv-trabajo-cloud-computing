package es.upv.posgrado.api.client.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.common.model.NewsStatus;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.CompletionStage;


@Slf4j
@ApplicationScoped
public class KafkaConsumer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    EventBus eventBus;

    @Incoming("hotnews")
    @Transactional
    @TransactionConfiguration(timeout = 2)
    @Blocking
    public CompletionStage<Void> hotNewsConsumer(Message<String> message) {
        try {
            String payload = message.getPayload();
            log.warn("receive new Message\n{}\n", payload);
            HotNews hotNews = objectMapper.readValue(payload, HotNews.class);
            hotNews.setStatus(NewsStatus.RECENT);
            hotNews.persistAndFlush();

            eventBus.publish("hotnews", objectMapper.writeValueAsString(hotNews));

        } catch (Exception e) {
            log.error("Fail processing the message", e);
        } finally {
            log.warn("Responding ACK");
            return message.ack();
        }
    }

    @Incoming("jobresponse")
    @Transactional
    @TransactionConfiguration(timeout = 2)
    @Blocking
    public CompletionStage<Void> jobResponseConsumer(Message<Job> message) {
        try {
            Job payload = message.getPayload();

            Optional<HotNews> hotNewsOptional = HotNews.findByIdOptional(payload.getId());
            if (hotNewsOptional.isPresent())
                payload.setThumbnail(hotNewsOptional.get().getThumbnail());

            log.warn("receive new Message\n{}\n", payload);
            Panache.getEntityManager().merge(payload);
            Panache.getEntityManager().flush();
        } catch (Exception e) {
            log.error("Fail in [jobResponseConsumer] processing the message", e);
            //dead letter channel?
        } finally {
            log.warn("Responding ACK");
            return message.ack();
        }
    }

}
