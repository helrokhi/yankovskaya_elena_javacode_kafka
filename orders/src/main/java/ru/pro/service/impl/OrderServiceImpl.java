package ru.pro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.kafka.OrderProducer;
import ru.pro.mapper.OrderMapper;
import ru.pro.model.dto.OrderCreateDto;
import ru.pro.model.dto.OrderDto;
import ru.pro.model.enums.OrderStatus;
import ru.pro.service.OrderService;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderProducer producer;
    private final OrderMapper mapper;
    private final Map<String, OrderDto> storage = new ConcurrentHashMap<>();

    @Override
    public OrderDto createOrder(OrderCreateDto dto) {
        OrderDto order = mapper.fromCreateDto(dto);
        storage.put(order.id(), order);
        producer.sendNewOrder(order.id(), order);
        log.info("Created new order {}", order.id());
        return order;
    }

    @Override
    public OrderDto updateStatus(String orderId, OrderStatus status) {
        OrderDto existing = storage.get(orderId);
        if (existing == null) {
            throw new NoSuchElementException("Order not found: " + orderId);
        }
        OrderDto updated = mapper.updateStatus(existing, status);
        storage.put(orderId, updated);
        log.info("Updated order {} status to {}", orderId, status);
        return updated;
    }
}
