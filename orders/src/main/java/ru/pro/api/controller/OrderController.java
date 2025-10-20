package ru.pro.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.pro.api.OrderApi;
import ru.pro.model.dto.OrderCreateDto;
import ru.pro.model.dto.OrderDto;
import ru.pro.model.enums.OrderStatus;
import ru.pro.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderDto> create(OrderCreateDto dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @Override
    public ResponseEntity<OrderDto> updateStatus(String id, OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
