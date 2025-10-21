package ru.pro.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.pro.api.PaymentApi;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {
    private final PaymentService paymentService;

    @Override
    public ResponseEntity<OrderDto> processPayment(OrderDto dto) {
        return ResponseEntity.ok(paymentService.processPayment(dto));
    }
}
