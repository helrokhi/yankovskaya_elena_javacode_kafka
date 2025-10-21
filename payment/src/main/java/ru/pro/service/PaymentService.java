package ru.pro.service;

import ru.pro.model.dto.OrderDto;

public interface PaymentService {
    OrderDto processPayment(OrderDto order);
}
