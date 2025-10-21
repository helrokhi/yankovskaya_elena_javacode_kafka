package ru.pro.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface OrderConsumer {
    void consume(Object object, ConsumerRecord<String, Object> consumerRecord);
}
