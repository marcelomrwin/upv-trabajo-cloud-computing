package es.upv.posgrado.observer.client.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.observer.service.ObserverService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class KafkaConsumer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ObserverService observerService;

    @Incoming("job-in")
    @Blocking
    public void submitJobRequest(ConsumerRecord<String,Job> record) {
        try {
            log.info("Registering job Request");
            Job job = record.value();
            observerService.registerJobOperation(job);
        } catch (Exception e) {
            log.error("Fail processing the message", e);
        }
    }

    @Incoming("job-out")
    @Blocking
    public void submitJobResponse(ConsumerRecord<String,Job> record) {
        try {
            log.info("Registering job Response");
            Job job = record.value();
            observerService.registerJobOperation(job);
        } catch (Exception e) {
            log.error("Fail processing the message", e);
        }
    }

}
