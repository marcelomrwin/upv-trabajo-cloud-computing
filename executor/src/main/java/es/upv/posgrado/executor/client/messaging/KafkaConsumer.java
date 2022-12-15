package es.upv.posgrado.executor.client.messaging;

import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import es.upv.posgrado.executor.service.ExecutorService;

import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class KafkaConsumer {

    @Inject
    ExecutorService executorService;

    @Inject
    EventBus eventBus;

    @Incoming("job-in")
//    @Transactional
    public CompletionStage<Void> hotNewsConsumer(Message<Job> message){
        try {
            Job job = message.getPayload();
            job.setStatus(JobStatus.PROCESSING);
            executorService.saveJob(job);
            eventBus.<Job>requestAndForget("job-request",job);
        } catch (Exception e) {
            log.error("Fail processing the message",e);
            return message.nack(e);
        }
      return message.ack();
    }
}
