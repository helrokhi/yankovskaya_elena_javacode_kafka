package ru.pro.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pro.model.dto.OrderCreateDto;
import ru.pro.model.dto.OrderDto;
import ru.pro.model.enums.OrderStatus;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RequestMapping("/api/v1/orders")
public interface OrderApi {
    @PostMapping
    default ResponseEntity<OrderDto> create(
            @RequestBody OrderCreateDto dto) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }

    @PatchMapping("/{id}/status")
    default ResponseEntity<OrderDto> updateStatus(
            @PathVariable String id,
            @RequestBody OrderStatus status) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
