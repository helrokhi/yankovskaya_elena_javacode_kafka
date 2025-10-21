package ru.pro.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.pro.model.dto.OrderDto;

import java.util.Map;

public interface ShippingConsumer {
    void consume(Object object, ConsumerRecord<String, Object> consumerRecord);

    Map<String, OrderDto> getPaidOrders();
}
