package ru.pro.service;

import ru.pro.model.dto.OrderCreateDto;
import ru.pro.model.dto.OrderDto;
import ru.pro.model.enums.OrderStatus;

public interface OrderService {
    OrderDto createOrder(OrderCreateDto dto);

    void updateStatus(String orderId, OrderStatus status);

    OrderDto findById(String id);
}
