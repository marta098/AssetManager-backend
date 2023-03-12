package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.mail.MailMessage;
import com.dhl.assetmanager.entity.Order;
import com.dhl.assetmanager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableAsync
public class MailService {

    private static final String UTF_8_ENCODING = "utf-8";

    private final TemplateService templateService;
    private final JavaMailSender mailSender;

    private static final String CHANGED_STATUS_TEMPLATE = "changed-status";
    private static final String NEW_ORDER_TEMPLATE = "new-order";
    private static final String DHL_REMINDER_TEMPLATE = "dhl-reminder";
    private static final String IT_REMINDER_TEMPLATE = "it-reminder";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${mail.sender}")
    private String sender;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Async
    public void sendChangingStatusUpdate(Order order) {
        var data = createChangeStatusTemplateData(order);
        var mailText = templateService.evaluate(CHANGED_STATUS_TEMPLATE, data);
        var title = String.format("Zamówienie %s zmieniło status", order.getOrderNumber());
        var mailMessage = new MailMessage(order.getReceiver().getEmail(), title, mailText);
        send(mailMessage);
    }

    @Async
    public void sendNewOrder(Order order) {
        var data = createNewOrderTemplateData(order);
        var mailText = templateService.evaluate(NEW_ORDER_TEMPLATE, data);
        var title = String.format("Nowe zamówienie %s", order.getOrderNumber());
        var mailMessage = new MailMessage(order.getReceiver().getEmail(), title, mailText);
        send(mailMessage);
    }

    @Async
    public void sendDhlReminder(Order order) {
        var data = createDhlReminderTemplateData(order);
        var mailText = templateService.evaluate(DHL_REMINDER_TEMPLATE, data);
        var title = String.format("Zamówienie %s czeka na potwierdzenie odbioru", order.getOrderNumber());
        var mailMessage = new MailMessage(order.getReceiver().getEmail(), title, mailText);
        send(mailMessage);
    }

    @Async
    public void sendItReminder(Order order) {
        var data = createItReminderTemplateData(order);
        var mailText = templateService.evaluate(IT_REMINDER_TEMPLATE, data);
        var title = String.format("Zamówienie %s nie zostało potwierdzone od ponad 28 dni", order.getOrderNumber());
        var mailMessage = new MailMessage(getAssignedToOrRequester(order).getEmail(), title, mailText);
        send(mailMessage);
    }


    private Map<String, Object> createChangeStatusTemplateData(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", order.getReceiver().getUsername());
        data.put("orderNumber", order.getOrderNumber());
        data.put("status", order.getStatus().getMessage());
        data.put("orderId", order.getId());
        data.put("url", frontendUrl);
        return data;
    }

    private Map<String, Object> createNewOrderTemplateData(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", order.getReceiver().getUsername());
        data.put("createdBy", order.getRequester().getUsername());
        data.put("model", order.getRequestedModel().getMessage());
        data.put("pickupDate", order.getPickupDate().format(DATE_TIME_FORMATTER));
        data.put("orderId", order.getId());
        data.put("url", frontendUrl);
        return data;
    }

    private Map<String, Object> createDhlReminderTemplateData(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", order.getReceiver().getUsername());
        data.put("orderNumber", order.getOrderNumber());
        data.put("orderId", order.getId());
        data.put("url", frontendUrl);
        return data;
    }

    private Map<String, Object> createItReminderTemplateData(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", getAssignedToOrRequester(order).getUsername());
        data.put("orderNumber", order.getOrderNumber());
        data.put("orderId", order.getId());
        data.put("url", frontendUrl);
        return data;
    }

    private void send(MailMessage message) {
        mailSender.send(mimeMessagePreparator(message));
    }

    private MimeMessagePreparator mimeMessagePreparator(MailMessage message) {
        return mimeMessage -> {
            var mailMessageHelper = new MimeMessageHelper(mimeMessage, UTF_8_ENCODING);
            mailMessageHelper.setFrom(sender);
            mailMessageHelper.setTo(message.getRecipient());
            mailMessageHelper.setSubject(message.getTitle());
            mailMessageHelper.setText(message.getText(), true);
        };
    }

    private User getAssignedToOrRequester(Order order) {
        return Optional.ofNullable(order.getAssignedTo())
                .orElse(order.getRequester());
    }

}
