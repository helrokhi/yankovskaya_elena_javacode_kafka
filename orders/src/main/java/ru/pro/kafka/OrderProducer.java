package ru.pro.kafka;

public interface OrderProducer {
    void sendNewOrder(String message, Object object);
}

