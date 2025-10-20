package ru.pro.kafka;

public interface OrderProducer {
    void sendNewOrder(String topic, String message, Object object);
}

