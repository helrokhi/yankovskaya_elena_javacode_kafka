package ru.pro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.kafka.PaymentProducer;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.PaymentService;

import static ru.pro.model.enums.OrderStatus.PAYED;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentProducer paymentProducer;

    @Override
    public OrderDto processPayment(OrderDto order) {
        log.info("Processing payment for order {}", order.id());

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
