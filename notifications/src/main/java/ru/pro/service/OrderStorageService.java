package ru.pro.service;

import ru.pro.model.dto.OrderDto;

import java.util.Map;

public interface OrderStorageService {
    void save(OrderDto order);

    Map<String, OrderDto> getAll();
}
