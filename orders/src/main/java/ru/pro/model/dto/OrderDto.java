package ru.pro.model.dto;

import ru.pro.model.enums.OrderStatus;

public record OrderDto(
        String id,
        String customer,
        String product,
        int quantity,
        OrderStatus status
) {
}
