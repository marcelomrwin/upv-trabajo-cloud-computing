package es.upv.posgrado.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.api.model.HotNews;
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
    ObjectMapper objectMapper;

    @Incoming("hotnews")
    @Transactional
    public CompletionStage<Void> hotNewsConsumer(Message<String> message){
        try {
            HotNews hotNews = objectMapper.readValue(message.getPayload(), HotNews.class);
            hotNews.persist();
        } catch (Exception e) {
            log.error("Fail processing the message",e);
            return message.nack(e);
        }
      return message.ack();
    }
}
