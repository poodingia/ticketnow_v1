package com.ticketnow.crud.service;

import com.ticketnow.crud.dto.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailConsumerService {

    private final EmailService emailService;
    private final ProducerService producerService;

    public KafkaEmailConsumerService(EmailService emailService, ProducerService producerService) {
        this.emailService = emailService;
        this.producerService = producerService;
    }

    @KafkaListener(topics = "payment_success_from_order", groupId = "email", containerFactory = "emailKafkaListenerContainerFactory")
    public void consume(OrderEmailDTO orderEmailDTO) {
        final EmailDTO emailDTO = emailService.handleEmailContent(orderEmailDTO);
        producerService.producePaymentSuccessMessage(emailDTO);
    }
}
