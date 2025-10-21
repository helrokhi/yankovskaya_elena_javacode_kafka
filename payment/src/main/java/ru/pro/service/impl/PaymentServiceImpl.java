package ru.pro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.kafka.PaymentConsumer;
import ru.pro.kafka.PaymentProducer;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.PaymentService;

import static ru.pro.model.enums.OrderStatus.NEW;
import static ru.pro.model.enums.OrderStatus.PAYED;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentProducer paymentProducer;
    private final PaymentConsumer paymentConsumer;

    @Override
    public OrderDto processPayment(String id) {
        log.info("Processing payment for order {}", id);

        OrderDto order = paymentConsumer.getOrders().get(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + id);
        }
        if (order.status() != NEW) {
            throw new IllegalStateException("Order is not in the NEW status: " + order.status());
        }

        OrderDto payed = new OrderDto(
                order.id(),
                order.customer(),
                PAYED
        );

        log.info("Payment successful for order {}", order.id());
        paymentProducer.sendPayedOrder(order.id(), payed);
        return payed;
    }
}
