package ru.pro.exception.wrappers;

import java.sql.Timestamp;
import java.util.Map;

public record ErrorResponse(
        int statusCode,
        String code,
        String message,
        Map<String, Object> details,
        Timestamp timestamp
) {}
