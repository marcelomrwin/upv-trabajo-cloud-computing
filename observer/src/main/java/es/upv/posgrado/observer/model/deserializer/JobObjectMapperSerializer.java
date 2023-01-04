package es.upv.posgrado.observer.model.deserializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkus.jackson.ObjectMapperCustomizer;

import javax.inject.Singleton;

@Singleton
public class JobObjectMapperSerializer implements ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
        objectMapper.enable(JsonParser.Feature.IGNORE_UNDEFINED);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
