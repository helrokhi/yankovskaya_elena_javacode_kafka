package ru.pro.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.pro.model.dto.OrderDto;

import java.util.Map;

public interface NotificationConsumer {
    void consume(Object object, ConsumerRecord<String, Object> consumerRecord);

    Map<String, OrderDto> getSentOrders();
}
