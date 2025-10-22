package ru.pro.model.dto;

public record OrderCreateDto(
        String customer,
        String product,
        int quantity) {
}
