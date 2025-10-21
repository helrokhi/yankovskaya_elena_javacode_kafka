package ru.pro.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pro.kafka.ShippingProducer;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShippingProducerImpl implements ShippingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topics.output}")
    private String topic;

    @Override
    public void sendSentOrder(String message, Object object) {
        log.info("send -> topic: {} message: {} object: {}", topic, message, object);
        kafkaTemplate.send(topic, message, object)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send message {} to Kafka to topic: {}", ex.getMessage(), topic);
                    } else {
                        log.info("Message {} sent successfully to topic: {}", message, topic);
                    }
                });
    }
}
