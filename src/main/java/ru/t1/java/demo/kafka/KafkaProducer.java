package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.MetricModel;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, MetricModel> template;


//    public void sendTo(String topic, MetricModel o, String error) {
//        ProducerRecord<String, MetricModel> record = new ProducerRecord<>(topic,"key", o);
//        record.headers().add("error-code", error.getBytes());
//        try {
//            template.send(record).get();
//            System.out.println("отправили в кафку - ");
//
//        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//        } finally {
//            template.flush();
//        }
//    }
    public void  sendTo(String topic,MetricModel model) {
        template.send(topic,model);
    }
}
