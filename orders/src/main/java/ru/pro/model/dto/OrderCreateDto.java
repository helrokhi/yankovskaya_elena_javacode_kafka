package ru.pro.model.dto;

import jakarta.validation.constraints.Email;

public record OrderCreateDto(
        @Email
        String customer,
        String product,
        int quantity) {
}
