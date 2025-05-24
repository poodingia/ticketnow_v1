package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.Event;
import com.ticketnow.crud.exception.NotFoundException;
import com.ticketnow.crud.repos.EventRepository;
import com.ticketnow.crud.utils.AuthenticationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;


@Service
public class QueueService {
    private final StringRedisTemplate redisTemplate;
    private final EventRepository eventRepository;
    private final Logger logger = LoggerFactory.getLogger(QueueService.class.getName());

    private static final String BOOKING_KEY_PREFIX = "booking_session:";
    private static final String BOOKING_COUNTER_PREFIX = "booking_counter:";
    private static final String WAITING_KEY_PREFIX = "waiting_session:";

    public QueueService(StringRedisTemplate redisTemplate, EventRepository eventRepository) {
        this.redisTemplate = redisTemplate;
        this.eventRepository = eventRepository;
    }

    private int insertToQueue(final int eventId, String sessionId, String bookingKey, String waitingKey, String bookingCounterKey) {
        int bookingCapacity = this.getEventCapacity(eventId);
        if (bookingCapacity <= 0) {
            redisTemplate.opsForValue().set(bookingKey, "active", Duration.ofMinutes(1));
            return 0;
        }
        Long activeBookings = redisTemplate.opsForValue().increment(bookingCounterKey, 0);
        if (activeBookings == null) activeBookings = 0L;

        if (activeBookings < bookingCapacity) {
            redisTemplate.opsForValue().set(bookingKey, "active", Duration.ofMinutes(1));
            redisTemplate.opsForValue().increment(bookingCounterKey, 1);
            logger.info("User {} moved from waiting queue to booking for event {}", sessionId, eventId);
            return 0;
        } else {
            long timestamp = System.currentTimeMillis(); // Use timestamp as score
            redisTemplate.opsForZSet().add(waitingKey, sessionId, timestamp);
            logger.info("No slots available. User {} moved to waiting queue for event {}.", sessionId, eventId);
            return Objects.requireNonNull(redisTemplate.opsForZSet().zCard(waitingKey)).intValue();
        }
    }

    public int getEventQueuePosition(int eventId) {
        String sessionId = AuthenticationUtils.extractSession();
        String bookingKey = BOOKING_KEY_PREFIX + eventId + ':' + sessionId;
        String waitingKey = WAITING_KEY_PREFIX + eventId;
        String bookingCounterKey = BOOKING_COUNTER_PREFIX + eventId;
        if (Objects.equals(redisTemplate.opsForValue().get(bookingKey), "active")) {
            return 0;
        } else if (redisTemplate.opsForZSet().rank(waitingKey, sessionId) != null) {
            return Objects.requireNonNull(redisTemplate.opsForZSet().rank(waitingKey, sessionId)).intValue() + 1;
        } else {
            return insertToQueue(eventId, sessionId, bookingKey, waitingKey, bookingCounterKey);
        }
    }

    private int getEventCapacity(int eventId) {
        String capacity = redisTemplate.opsForValue().get("event:capacity:" + eventId);
        if (capacity != null) {
            return Integer.parseInt(capacity);
        }
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        redisTemplate.opsForValue().set("event:capacity:" + eventId, String.valueOf(event.getBookingCapacity()));
        return event.getBookingCapacity();
    }
}
