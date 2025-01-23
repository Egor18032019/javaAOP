package ru.t1.java.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.java.demo.util.TopicName;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DemoKafkaConfiguration {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker_1_t1:9092");
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public NewTopic metricsTopic() {
        return new NewTopic(TopicName.T1_METRICS_TOPIC, 1, (short) 1);
    }
    @Bean
    public NewTopic accountsTopic() {
        return new NewTopic(TopicName.T1_DEMO_ACCOUNTS, 1, (short) 1);
    }
    @Bean
    public NewTopic transactionTopic() {
        return new NewTopic(TopicName.T1_DEMO_TRANSACTIONS, 1, (short) 1);
    }
}