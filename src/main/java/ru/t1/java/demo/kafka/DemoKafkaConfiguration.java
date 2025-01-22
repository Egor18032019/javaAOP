package ru.t1.java.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.java.demo.util.TopicName;

@Slf4j
@Configuration
public class DemoKafkaConfiguration {


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