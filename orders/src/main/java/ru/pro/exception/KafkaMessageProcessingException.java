package ru.pro.exception;

import lombok.Getter;
import org.springframework.messaging.MessagingException;

@Getter
public class KafkaMessageProcessingException extends MessagingException {
    private final String topic;
    private final Object payload;

    public KafkaMessageProcessingException(String message, String topic, Object payload, Throwable cause) {
        super(message, cause);
        this.topic = topic;
        this.payload = payload;
    }
}
