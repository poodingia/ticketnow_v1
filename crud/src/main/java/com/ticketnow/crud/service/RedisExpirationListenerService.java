package com.ticketnow.crud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
public class RedisExpirationListenerService implements MessageListener {
    private final StringRedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisExpirationListenerService.class);
    private static final String BOOKING_KEY_PREFIX = "booking_session:";
    private static final String BOOKING_COUNTER_PREFIX = "booking_counter:";
    private static final String WAITING_KEY_PREFIX = "waiting_session:";

    public RedisExpirationListenerService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // Expired Redis key
        logger.info("Session expired: {}", expiredKey);
        if (expiredKey.startsWith("booking_session:")) {
            String eventId = expiredKey.split(":")[1];
            String waitingKey = WAITING_KEY_PREFIX + eventId;
            String bookingCounterKey = BOOKING_COUNTER_PREFIX + eventId;

            redisTemplate.opsForValue().decrement(bookingCounterKey, 1);
            String nextSession = popOldestFromQueue(waitingKey);
            if (nextSession != null) {
                String bookingKey = BOOKING_KEY_PREFIX + eventId + ':' + nextSession;
                redisTemplate.opsForValue().increment(bookingCounterKey, 1);
                redisTemplate.opsForValue().set(bookingKey, "active", Duration.ofMinutes(1));
                logger.info("Moved session {} from waiting queue to active booking for event {}", nextSession, eventId);
            }
        }
    }

    public String popOldestFromQueue(String key) {
        Set<String> result = redisTemplate.opsForZSet().range(key, 0, 0); // Get first (oldest) element
        if (result != null && !result.isEmpty()) {
            String oldest = result.iterator().next();
            redisTemplate.opsForZSet().remove(key, oldest); // Remove it
            return oldest;
        }
        return null;
    }
}
