package com.ticketnow.account.service;

import com.ticketnow.account.domain.UserEventFollow;
import com.ticketnow.account.dto.CrudEmailDTO;
import com.ticketnow.account.repos.UserEventFollowRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService {
    private static final String EVENT_START_SALE_TOPIC = "event_start_sale_notification";
    private static final String PAYMENT_SUCCESS_TOPIC = "payment_success_from_crud";
    private static final String TOPIC_EVENT_CHANGE = "event_change_from_crud";
    private final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaConsumerService.class);
    private final EmailService emailService;
    private final UserEventFollowRepository userEventFollowRepository;
    private final AccountService accountService;

    public KafkaConsumerService(EmailService emailService, UserEventFollowRepository userEventFollowRepository, AccountService accountService) {
        this.emailService = emailService;
        this.userEventFollowRepository = userEventFollowRepository;
        this.accountService = accountService;
    }

    @KafkaListener(topics = EVENT_START_SALE_TOPIC, groupId = "account", containerFactory = "stringKafkaListenerContainerFactory")
    public void consume(@Payload String eventIds) throws MessagingException {
        if (eventIds == null || eventIds.isBlank()) {
            log.warn("Received empty message");
            return;
        }
        String[] parts = eventIds.split(":");
        List<String> userIds = userEventFollowRepository.findAllUserIdsByEventId(Integer.valueOf(parts[0])).stream()
                .map(UserEventFollow::getUserId).toList();
        String[] emails = accountService.getAccountEmail(userIds);
        emailService.sendEventSaleNotification(parts[1], "Tommy", emails);
        log.info("Events starting sales tomorrow: {}", eventIds);
    }

    @KafkaListener(topics = PAYMENT_SUCCESS_TOPIC, groupId = "account")
    public void consume(CrudEmailDTO crudEmailDTO) throws Exception {
        emailService.sendPaymentEmail(crudEmailDTO);
    }

    @KafkaListener(topics = TOPIC_EVENT_CHANGE, groupId = "account", containerFactory = "stringKafkaListenerContainerFactory")
    public void notifyEventChange(@Payload String message) throws MessagingException {
        String[] parts = message.split(":");
        List<String> userIds = userEventFollowRepository.findAllUserIdsByEventId(Integer.valueOf(parts[0])).stream()
                .map(UserEventFollow::getUserId).toList();
        if (userIds.isEmpty()) {
            return;
        }
        String[] emails = accountService.getAccountEmail(userIds);
        emailService.sendEventChangeNotification(parts[1], emails);
    }
}
