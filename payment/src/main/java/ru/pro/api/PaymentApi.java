package ru.pro.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pro.model.dto.OrderDto;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RequestMapping("/api/v1/payments")
public interface PaymentApi {
    @PostMapping("/process")
    default ResponseEntity<OrderDto> processPayment(@RequestBody OrderDto dto) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
