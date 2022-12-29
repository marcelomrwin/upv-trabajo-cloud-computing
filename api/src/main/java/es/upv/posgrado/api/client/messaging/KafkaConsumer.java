package es.upv.posgrado.api.client.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.common.model.NewsStatus;
import es.upv.posgrado.common.model.monitoring.MonitoringResponse;
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
            log.debug("receive new Message\n{}\n", payload);
            HotNews hotNews = objectMapper.readValue(payload, HotNews.class);
            hotNews.setStatus(NewsStatus.RECENT);
            hotNews.persistAndFlush();

            eventBus.publish("hotnews", objectMapper.writeValueAsString(hotNews));

        } catch (Exception e) {
            log.error("Fail processing the message", e);
        } finally {
            return message.ack();
        }
    }

    @Incoming("jobresponse")
    @Transactional
    @TransactionConfiguration(timeout = 2)
    @Blocking
    public CompletionStage<Void> jobResponseConsumer(Message<Job> message) {
        try {
            Job job = message.getPayload();
            log.debug("receive new Message\n{}\n", job);
            Panache.getEntityManager().merge(job);
            Panache.getEntityManager().flush();

            es.upv.posgrado.common.model.Job dto = es.upv.posgrado.common.model.Job.builder()
                    .id(String.valueOf(job.getId()))
                    .status(job.getStatus())
                    .submittedBy(job.getSubmittedBy())
                    .build();

            eventBus.publish("job", objectMapper.writeValueAsString(dto));

        } catch (Exception e) {
            log.error("Fail in [jobResponseConsumer] processing the message", e);
            //TODO dead letter channel?
        } finally {
            return message.ack();
        }
    }

    //mp.messaging.incoming.job-stats.topic=job-statistics
    @Incoming("job-stats")
    public CompletionStage<Void> jobStatisticsConsumer(Message<MonitoringResponse> message) {
        try {
            MonitoringResponse stats = message.getPayload();

            //TODO sent history to the client
            if (stats.getStatisticsHistory() != null)
                stats.getStatisticsHistory().clear();
            if (stats.getEventHistory() != null)
                stats.getEventHistory().clear();

            log.debug("receive message and publish stats\n{}\n", stats);
            eventBus.publish("job-stats", objectMapper.writeValueAsString(stats));

        } catch (Exception e) {
            log.error("Fail in [jobStatisticsConsumer] processing the message", e);
            //TODO dead letter channel?
        } finally {
            return message.ack();
        }
    }
}
