package ru.pro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.kafka.ShippingConsumer;
import ru.pro.kafka.ShippingProducer;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.ShippingService;

import static ru.pro.model.enums.OrderStatus.PAYED;
import static ru.pro.model.enums.OrderStatus.SENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {
    private final ShippingProducer shippingProducer;
    private final ShippingConsumer shippingConsumer;

    @Override
    public OrderDto processSent(String id) {
        log.info("Processing sent for order {}", id);
        OrderDto order = shippingConsumer.getPaidOrders().get(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + id);
        }
        if (order.status() != PAYED) {
            throw new IllegalStateException("Order is not in the PAYED status: " + order.status());
        }

        OrderDto payed = new OrderDto(
                order.id(),
                order.customer(),
                SENT
        );

        log.info("Sent successful for order {}", order.id());
        shippingProducer.sendSentOrder(order.id(), payed);
        return payed;
    }
}
