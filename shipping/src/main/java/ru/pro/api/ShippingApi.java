package ru.pro.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pro.model.dto.OrderDto;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RequestMapping("/api/v1/orders")
public interface ShippingApi {
    @PostMapping("/{id}/send")
    default ResponseEntity<OrderDto> processSent(@PathVariable String id) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
