package ru.pro.kafka;

public interface PaymentProducer {
    void sendPayedOrder(String message, Object object);
}
