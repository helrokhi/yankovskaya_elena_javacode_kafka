package ru.pro.exception.handlers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.pro.exception.ApiException;
import ru.pro.exception.KafkaMessageProcessingException;
import ru.pro.exception.wrappers.ErrorResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {

        Map<String, Object> details = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException manv) {
            // Ошибки DTO (@RequestBody)
            manv.getBindingResult().getFieldErrors()
                    .forEach(fe -> details.put(fe.getField(),
                            Map.of(
                                    "rejectedValue", Optional.ofNullable(fe.getRejectedValue()).orElse("null"),
                                    "message", Objects.requireNonNull(fe.getDefaultMessage())
                            )));
        } else if (ex instanceof ConstraintViolationException cve) {
            // Ошибки параметров метода (@PathVariable, @RequestParam)
            cve.getConstraintViolations().forEach(cv -> {
                String field = cv.getPropertyPath().toString();
                details.put(field,
                        Map.of(
                                "rejectedValue", cv.getInvalidValue(),
                                "message", cv.getMessage()
                        ));
            });
        }

        logAtLevel(BAD_REQUEST, "VALIDATION_ERROR", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Некорректный формат запроса",
                Map.copyOf(details),
                now()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode());
        logAtLevel(status, ex.getCode(), ex);
        return buildResponse(
                ex.getStatusCode(),
                ex.getCode(),
                ex.getMessage(),
                ex.getDetails(),
                ex.getTimestamp()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(NoSuchElementException ex) {
        return handleError(NOT_FOUND, "ELEMENT_NOT_FOUND", ex.getMessage(), ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return handleError(BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getMostSpecificCause();
        String message = rootCause.getMessage();
        return handleError(BAD_REQUEST, "INVALID_JSON", message, ex);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleMessagingException(MessagingException ex) {
        log.error("Ошибка отправки/приема сообщения", ex);
        return buildResponse(
                INTERNAL_SERVER_ERROR.value(),
                "MESSAGING_ERROR",
                "Ошибка обработки сообщения (MessagingException). Попробуйте позже.",
                Map.of("error", ex.getMessage()),
                now()
        );
    }

    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<ErrorResponse> handleSerializationException(SerializationException ex) {
        log.error("Ошибка сериализации/десериализации сообщения", ex);
        return buildResponse(
                BAD_REQUEST.value(),
                "SERIALIZATION_ERROR",
                "Ошибка сериализации/десериализации сообщения.",
                Map.of("error", ex.getMessage()),
                now()
        );
    }

    @ExceptionHandler(ListenerExecutionFailedException.class)
    public ResponseEntity<ErrorResponse> handleListenerExecutionFailed(ListenerExecutionFailedException ex) {
        log.error("Ошибка выполнения KafkaListener", ex);
        return buildResponse(
                INTERNAL_SERVER_ERROR.value(),
                "LISTENER_FAILED",
                "Ошибка выполнения обработки события Kafka.",
                Map.of("error", ex.getMessage()),
                now()
        );
    }

    @ExceptionHandler(KafkaMessageProcessingException.class)
    public ResponseEntity<ErrorResponse> handleKafkaMessageProcessing(KafkaMessageProcessingException ex) {
        log.error("Kafka message error on topic {}: {}", ex.getTopic(), ex.getMessage(), ex);
        return buildResponse(
                INTERNAL_SERVER_ERROR.value(),
                "KAFKA_MESSAGE_ERROR",
                ex.getMessage(),
                Map.of(
                        "topic", ex.getTopic(),
                        "payload", String.valueOf(ex.getPayload())
                ),
                now()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        log.error("Необработанное исключение", ex);
        return buildResponse(
                INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Произошла внутренняя ошибка. Попробуйте позже.",
                Map.of(),
                now()
        );
    }

    private ResponseEntity<ErrorResponse> handleError(
            HttpStatus status, String code, String message, Throwable ex) {
        log.warn(code, ex);
        return buildResponse(status.value(), code, message, Map.of(), now());
    }

    private ResponseEntity<ErrorResponse> buildResponse(int statusCode,
                                                        String code,
                                                        String message,
                                                        Map<String, Object> details,
                                                        Timestamp timestamp) {
        ErrorResponse error = new ErrorResponse(
                statusCode,
                code,
                message,
                details,
                timestamp
        );
        return ResponseEntity.status(statusCode).body(error);
    }

    private void logAtLevel(HttpStatus status, String code, Throwable ex) {
        if (status.is4xxClientError()) {
            log.warn("{}: {}", code, ex.getMessage());
        } else {
            log.error("{}: {}", code, ex.getMessage());
        }
    }

    private Timestamp now() {
        return Timestamp.from(Instant.now());
    }
}
