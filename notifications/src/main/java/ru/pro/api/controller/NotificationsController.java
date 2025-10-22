package ru.pro.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.pro.api.NotificationsApi;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.NotificationService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class NotificationsController implements NotificationsApi {
    private final NotificationService notificationService;

    @Override
    public ResponseEntity<Set<OrderDto>> findAllByCustomer(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.findAllByCustomer(id));
    }
}
