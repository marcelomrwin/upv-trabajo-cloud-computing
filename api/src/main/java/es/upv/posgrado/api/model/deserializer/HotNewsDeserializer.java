package es.upv.posgrado.api.model.deserializer;

import es.upv.posgrado.api.model.HotNews;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class HotNewsDeserializer extends ObjectMapperDeserializer<HotNews> {
    public HotNewsDeserializer() {
        super(HotNews.class);
    }
}
