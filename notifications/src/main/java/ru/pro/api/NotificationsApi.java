package ru.pro.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pro.model.dto.OrderDto;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RequestMapping("/api/v1/customers")
public interface NotificationsApi {
    @GetMapping("/{id}/notifications")
    default ResponseEntity<Set<OrderDto>> findAllByCustomer(@PathVariable String id) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
