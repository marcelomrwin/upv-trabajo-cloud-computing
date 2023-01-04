package es.upv.posgrado.executor.client.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.service.ExecutorService;

import es.upv.posgrado.executor.service.NewsService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class KafkaConsumer {

    @Inject
    ExecutorService executorService;

    @Inject
    NewsService newsService;

    @Inject
    ObjectMapper objectMapper;

    @Incoming("job-in")
    @Blocking
    @Transactional
    public CompletionStage<Void> submitJobConsumer(Message<Job> message){
        try {
            Job job = message.getPayload();
            job.setStatus(JobStatus.PROCESSING);
            CompletableFuture<Void> completableFuture = executorService.consumeJobEvent(job);

        } catch (Exception e) {
            log.error("Fail processing the message",e);
            return message.nack(e);
        }
      return message.ack();
    }

    @Incoming("hotnews")
    @Transactional
    @Blocking
    public CompletionStage<Void> hotNewsConsumer(Message<String> message) {
        try {
            String payload = message.getPayload();
            log.debug("receive new Message\n{}\n", payload);
            NewsDTO hotNews = objectMapper.readValue(payload, NewsDTO.class);
            newsService.saveNews(hotNews);
        } catch (Exception e) {
            log.error("Fail processing the message", e);
        } finally {
            log.debug("Responding ACK");
            return message.ack();
        }
    }
}
