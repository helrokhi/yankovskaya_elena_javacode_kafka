package ru.pro.service;

import ru.pro.model.dto.OrderDto;

public interface ShippingService {
    OrderDto processSent(String id);
}
