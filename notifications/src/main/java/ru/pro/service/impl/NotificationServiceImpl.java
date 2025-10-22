package ru.pro.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.pro.model.dto.OrderDto;
import ru.pro.service.NotificationService;
import ru.pro.service.OrderStorageService;

import java.util.Set;
import java.util.stream.Collectors;

import static ru.pro.model.enums.OrderStatus.SENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final OrderStorageService orderStorageService;
    private final JavaMailSender mailSender;

    @Override
    public Set<OrderDto> findAllByCustomer(String id) {
        return orderStorageService.getAll().values().parallelStream()
                .filter(order -> order.customer().equals(id) && order.status().equals(SENT))
                .collect(Collectors.toSet());
    }

    @Override
    public void sendOrderDeliveredNotification(OrderDto order) {
        try {
            sendEmail(order);
            log.info("Notification sent to user {} about delivery of order {}", order.customer(), order.id());
        } catch (MessagingException e) {
            log.error("Ошибка при отправке уведомления пользователю {}: {}", order.customer(), e.getMessage());
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при отправке уведомления для заказа {}: {}", order.id(), e.getMessage());
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
