package ru.pro.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.pro.kafka.PaymentConsumer;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.PaymentService;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumerImpl implements PaymentConsumer {
    private final PaymentService paymentService;

    @Override
    @KafkaListener(
            topics = "${topics.input}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "objectConcurrentKafkaListenerContainerFactory")
    public void consume(
            @Payload Object object,
            ConsumerRecord<String, Object> consumerRecord) {
        handleRequest(consumerRecord.value());
    }

    private void handleRequest(Object value) {
        if (!(value instanceof OrderDto)) {
            log.warn("Expected OrderDto, got: {}", value.getClass());
            return;
        }
        OrderDto order = (OrderDto) value;
        log.info("Received new order: {}", order.id());
        paymentService.processPayment(order);
    }
}
