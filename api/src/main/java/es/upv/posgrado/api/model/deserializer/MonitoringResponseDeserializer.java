package es.upv.posgrado.api.model.deserializer;

import es.upv.posgrado.common.model.monitoring.MonitoringResponse;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class MonitoringResponseDeserializer extends ObjectMapperDeserializer<MonitoringResponse> {
    public MonitoringResponseDeserializer() {
        super(MonitoringResponse.class);
    }
}
