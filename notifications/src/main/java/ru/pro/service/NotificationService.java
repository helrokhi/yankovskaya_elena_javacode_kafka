package ru.pro.service;

import jakarta.mail.MessagingException;
import ru.pro.model.dto.OrderDto;

import java.util.Set;

public interface NotificationService {
    Set<OrderDto> findAllByCustomer(String id);

    void sendOrderDeliveredNotification(OrderDto order) throws MessagingException;
}
