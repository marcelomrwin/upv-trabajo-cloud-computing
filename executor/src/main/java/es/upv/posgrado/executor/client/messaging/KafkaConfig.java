package es.upv.posgrado.executor.client.messaging;


import es.upv.posgrado.common.model.Job;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@ApplicationScoped
public class KafkaConfig {
    @ConfigProperty(name = "kafka.bootstrap.servers")
    String kafkaBrokers;

    @ConfigProperty(name = "app.producer.clienId")
    String producerClientId;

    @ConfigProperty(name = "app.producer.acks")
    String acks;


    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

    @Produces
    @RequestScoped
    public Producer<String, Job> createProducer() {
        var props = new Properties();

        // Kafka Bootstrap
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);

        // Producer Client
        props.putIfAbsent(ProducerConfig.CLIENT_ID_CONFIG, producerClientId + "-" + getHostname());

        // Serializer for Keys and Values
        props.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectMapperSerializer.class.getName());

        // Acknowledgement
        props.putIfAbsent(ProducerConfig.ACKS_CONFIG, acks);

        props.putIfAbsent(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,3719907);

        return new KafkaProducer<>(props);
    }
}