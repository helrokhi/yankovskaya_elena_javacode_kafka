package ru.pro.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.pro.exception.KafkaMessageProcessingException;
import ru.pro.kafka.PaymentConsumer;
import ru.pro.model.dto.OrderDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.pro.model.enums.OrderStatus.NEW;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class PaymentConsumerImpl implements PaymentConsumer {
    private final Map<String, OrderDto> newOrders = new ConcurrentHashMap<>();

    @Override
    @KafkaListener(
            topics = "${topics.input}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "objectConcurrentKafkaListenerContainerFactory")
    public void consume(
            @Payload Object object,
            ConsumerRecord<String, Object> consumerRecord) {
        try {
            Object value = consumerRecord.value();
            log.info("Получено сообщение из Kafka. Topic='{}', Value='{}'",
                    consumerRecord.topic(), value);

            handleRequest(value, consumerRecord);
        } catch (KafkaMessageProcessingException ex) {
            log.error("Ошибка обработки сообщения Kafka: {}", ex.getMessage(), ex);
            throw ex;
        } catch (JsonProcessingException ex) {
            throw new KafkaMessageProcessingException(
                    "Ошибка десериализации Kafka-сообщения",
                    consumerRecord.topic(),
                    consumerRecord.value(),
                    ex
            );
        } catch (Exception ex) {
            throw new KafkaMessageProcessingException(
                    "Неизвестная ошибка при обработке Kafka-сообщения",
                    consumerRecord.topic(),
                    consumerRecord.value(),
                    ex
            );
        }
    }

    private void handleRequest(
            Object value,
            ConsumerRecord<String, Object> record) throws JsonProcessingException {
        if (!(value instanceof OrderDto)) {
            log.warn("Ожидался тип OrderDto, получен: {}", (value != null ? value.getClass() : "null"));
            throw new KafkaMessageProcessingException(
                    "Некорректный тип данных в сообщении Kafka",
                    record.topic(),
                    value,
                    null
            );
        }
        OrderDto order = (OrderDto) value;
        if (order.status() != NEW) {
            log.info("Пропуск заказа: статус заказа {} не равен NEW", order.status());
            return;
        }

        log.info("Обрабатывается заказ id={} для клиента={}", order.id(), order.customer());
        newOrders.put(order.id(), order);
    }
}
