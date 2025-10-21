package ru.pro.kafka;

public interface ShippingProducer {
    void sendSentOrder(String message, Object object);
}
