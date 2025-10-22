package ru.pro.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.pro.kafka.NotificationConsumer;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.NotificationService;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.pro.model.enums.OrderStatus.SENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationConsumer notificationConsumer;
    private final JavaMailSender mailSender;

    @Override
    public Set<OrderDto> findAllByCustomer(String id) {
        Map<String, OrderDto> sentOrders = notificationConsumer.getSentOrders();
        return sentOrders.values().stream()
                .filter(order -> order.customer().equals(id))
                .filter(order -> order.status().equals(SENT))
                .collect(Collectors.toSet());
    }

    @Override
    public void sendOrderDeliveredNotification(OrderDto order) {
        try {
            sendEmail(order);
            log.info("Notification sent to user {} about delivery of order {}", order.customer(), order.id());
        } catch (Exception e) {
            log.error("Unable to send notification for order {}: {}", order.id(), e.getMessage());
        }
    }

    private void sendEmail(OrderDto order) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(order.customer());
        helper.setSubject("Your order has been delivered.");
        helper.setText(String.format("Order with ID %s successfully delivered!", order.id()));

        mailSender.send(message);
    }
}
