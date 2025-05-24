package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.Event;
import com.ticketnow.crud.dto.TicketTypePrepareDTO;
import com.ticketnow.crud.repos.EventRepository;
import com.ticketnow.crud.repos.TicketTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {
    private final EventRepository eventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String EVENT_START_SALE_TOPIC = "event_start_sale_notification";
    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    private final TicketTypeRepository ticketTypeRepository;

    public ScheduleService(EventRepository eventRepository, KafkaTemplate<String, String> kafkaTemplate, RedisTemplate<String, String> redisTemplate, TicketTypeRepository ticketTypeRepository) {
        this.eventRepository = eventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendEmail() {
        List<Event> events = getAllEventsOpenSaleTomorrow();

        if (events.isEmpty()) {
            log.info("No events starting sales tomorrow.");
            return;
        }
        for (Event event : events) {
            String message = event.getId().toString().concat(":").concat(event.getTitle());
            kafkaTemplate.send(EVENT_START_SALE_TOPIC, message);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cacheEventBookingData() {
        log.info("Caching event booking data.");
        List<TicketTypePrepareDTO> ticketTypes = getTicketTypesForEventsStartingTomorrow();
        for (TicketTypePrepareDTO ticketType : ticketTypes) {
            redisTemplate.opsForValue().set("ticket_left:" + ticketType.id(), Integer.toString(ticketType.quantity()));
            redisTemplate.opsForSet().add("event_ticket_types:" + ticketType.eventId(), Integer.toString(ticketType.quantity()));
            redisTemplate.opsForValue().set("price:" + ticketType.id(), Float.toString(ticketType.price()));
        }

    }

    private List<Event> getAllEventsOpenSaleTomorrow() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime startOfDay = tomorrow.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = tomorrow.withHour(23).withMinute(59).withSecond(59);

        return eventRepository.findAllByStartSaleDateBetween(startOfDay, endOfDay);
    }

    private List<TicketTypePrepareDTO> getTicketTypesForEventsStartingTomorrow() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime startOfDay = tomorrow.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = tomorrow.withHour(23).withMinute(59).withSecond(59);

        return ticketTypeRepository.findTicketTypesForEventsStartingTomorrow(startOfDay, endOfDay);
    }

}
