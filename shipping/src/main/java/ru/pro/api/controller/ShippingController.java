package ru.pro.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.pro.api.ShippingApi;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.ShippingService;

@RestController
@RequiredArgsConstructor
public class ShippingController implements ShippingApi {
    private final ShippingService shippingService;

    @Override
    public ResponseEntity<OrderDto> processSent(String id) {
        return ResponseEntity.ok(shippingService.processSent(id));
    }
}
