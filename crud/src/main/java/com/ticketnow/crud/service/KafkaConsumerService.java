package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.Coupon;
import com.ticketnow.crud.dto.CouponKafkaDTO;
import com.ticketnow.crud.exception.NotFoundException;
import com.ticketnow.crud.repos.CouponRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KafkaConsumerService {
    private static final Logger log = LogManager.getLogger(KafkaConsumerService.class);
    private final CouponRepository couponRepository;
    private final TicketTypeService ticketTypeService;
    private final String TOPIC_UPDATE_COUPON = "update_coupon_from_order";
    private final String TOPIC_UPDATE_TICKET = "update_ticket_from_order";
    private final EventService eventService;

    public KafkaConsumerService(CouponRepository couponRepository, TicketTypeService ticketTypeService, EventService eventService) {
        this.couponRepository = couponRepository;
        this.ticketTypeService = ticketTypeService;
        this.eventService = eventService;
    }

    @KafkaListener(topics = TOPIC_UPDATE_COUPON, groupId = "coupon", containerFactory = "CouponKafkaListenerContainerFactory")
    public void updateCouponUsage(@Payload CouponKafkaDTO couponKafkaDTO) {
        Coupon coupon = couponRepository.findById(Integer.valueOf(couponKafkaDTO.getCouponCode())).orElseThrow(NotFoundException::new);
        coupon.setUsed(coupon.getUsed() + 1);
        couponRepository.save(coupon);
    }

    @KafkaListener(topics = TOPIC_UPDATE_TICKET, groupId = "ticket", containerFactory = "StringKafkaListenerContainerFactory")
    public void updateTicketQuantity(String message) {
        if (message == null || message.isBlank()) {
            log.warn("Received empty message");
            return;
        }

        String[] parts = message.split(",");
        if (parts.length < 2) {
            log.warn("Invalid message format: {}", message);
            return;
        }
        log.info("Received message: {}", message);

        Integer eventId = parseIntSafely(parts[0]);
        Float revenue = Float.parseFloat(parts[parts.length - 1]);


        Map<Integer, Integer> ticketMap = Arrays.stream(parts)
                .skip(1)
                .limit(parts.length - 2)
                .map(part -> part.split(":"))
                .filter(ticketInfo -> ticketInfo.length == 2)
                .collect(Collectors.toMap(
                        ticketInfo -> parseIntSafely(ticketInfo[0]),
                        ticketInfo -> parseIntSafely(ticketInfo[1]),
                        (existing, replacement) -> replacement
                ));

        if (ticketMap.isEmpty()) {
            log.info("No valid ticket information found in message");
            return;
        }

        ticketTypeService.updateTicketTypes(ticketMap);
        eventService.updateRevenue(eventId, revenue);
    }

    private int parseIntSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("Failed to parse integer value: {}", value, e);
            return 0;
        }
    }

}
