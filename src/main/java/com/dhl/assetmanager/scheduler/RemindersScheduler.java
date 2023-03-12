package com.dhl.assetmanager.scheduler;

import com.dhl.assetmanager.entity.ChangeHistory;
import com.dhl.assetmanager.entity.Order;
import com.dhl.assetmanager.exception.TechnicalException;
import com.dhl.assetmanager.repository.OrderRepository;
import com.dhl.assetmanager.service.MailService;
import com.dhl.assetmanager.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemindersScheduler {

    private final MailService mailService;
    private final DateUtil dateUtil;

    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 12 * * * *")
    public void scanForReminders() {
        var orders = orderRepository.findAllOrdersWithSentStatus();
        handleDhlReminders(orders);
        handleItReminders(orders);
    }

    private void handleDhlReminders(List<Order> orders) {
        orders.stream()
                .filter(order -> {
                    var latestChangeTimestamp = getLastChangeHistory(order).getTimestamp();
                    return isIdle(latestChangeTimestamp, 14);
                })
                .filter(order -> !order.getIsDhlReminderSent())
                .forEach(order -> {
                    mailService.sendDhlReminder(order);

                    order.setIsDhlReminderSent(true);
                    orderRepository.save(order);
                });
    }

    private void handleItReminders(List<Order> orders) {
        orders.stream()
                .filter(order -> {
                    var latestChangeTimestamp = getLastChangeHistory(order).getTimestamp();
                    return isIdle(latestChangeTimestamp, 28);
                })
                .filter(order -> !order.getIsItReminderSent())
                .forEach(order -> {
                    mailService.sendItReminder(order);

                    order.setIsItReminderSent(true);
                    orderRepository.save(order);
                });
    }

    private ChangeHistory getLastChangeHistory(Order order) {
        return order.getChangeHistory().stream()
                .max(Comparator.comparing(ChangeHistory::getTimestamp))
                .orElseThrow(TechnicalException::new);
    }

    private boolean isIdle(LocalDateTime timestamp, int days) {
        var currentTimestamp = dateUtil.getCurrentDateTime();
        return currentTimestamp.minusDays(days).isAfter(timestamp);
    }

}
