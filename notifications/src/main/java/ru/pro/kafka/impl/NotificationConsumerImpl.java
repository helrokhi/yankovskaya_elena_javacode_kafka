package ru.pro.kafka.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.pro.kafka.NotificationConsumer;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.NotificationService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerImpl implements NotificationConsumer {
    private final NotificationService notificationService;
    @Getter
    private final Map<String, OrderDto> sentOrders = new ConcurrentHashMap<>();

    @Override
    @KafkaListener(
            topics = "${topics.input.sent}",
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
        sentOrders.put(order.id(), order);
        notificationService.sendOrderDeliveredNotification(order);
    }
}