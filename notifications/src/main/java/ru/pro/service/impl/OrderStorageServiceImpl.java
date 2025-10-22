package ru.pro.service.impl;

import org.springframework.stereotype.Service;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.OrderStorageService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderStorageServiceImpl implements OrderStorageService {
    private final Map<String, OrderDto> sentOrders = new ConcurrentHashMap<>();

    public void save(OrderDto order) {
        sentOrders.put(order.id(), order);
    }

    public Map<String, OrderDto> getAll() {
        return sentOrders;
    }
}
