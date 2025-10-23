package ru.pro.exception;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@Getter
public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String code;
    private final String message;
    private final Map<String, Object> details;
    private final Timestamp timestamp;

    public ApiException(int statusCode, String code, String message, Map<String, Object> details) {
        super(message);
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.details = details != null ? details : Map.of();
        this.timestamp = Timestamp.from(Instant.now());
    }
}
