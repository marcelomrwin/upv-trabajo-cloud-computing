package es.upv.posgrado.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.api.model.HotNews;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.util.concurrent.CompletionStage;


@Slf4j
@ApplicationScoped
public class KafkaConsumer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    UserTransaction transaction;

    @Incoming("hotnews")
    @Transactional
    @TransactionConfiguration(timeout = 2)
    @Blocking
    public CompletionStage<Void> hotNewsConsumer(Message<String> message) {
        try {
            log.warn("UserTransaction {}",transaction);
            String payload = message.getPayload();
            log.warn("receive new Message\n{}\n", payload);
            HotNews hotNews = objectMapper.readValue(payload, HotNews.class);
            hotNews.persistAndFlush();
        } catch (Exception e) {
            log.error("Fail processing the message", e);
        } finally {
            log.warn("Responding ACK");
            return message.ack();
        }
    }

}
