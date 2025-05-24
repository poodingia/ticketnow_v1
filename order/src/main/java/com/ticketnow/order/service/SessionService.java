package com.ticketnow.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private final StringRedisTemplate redisTemplate;

    public SessionService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String BOOKING_KEY_PREFIX = "booking_session:";
    private static final String BOOKING_COUNTER_KEY = "booking_counter:";
    private static final String WAITING_KEY_PREFIX = "waiting_session:";

    public boolean checkValidSession(String sessionId, int eventId) {
        log.info("Checking session: {}", sessionId);
        String bookingKey = BOOKING_KEY_PREFIX + eventId + ':' + sessionId;
        log.info("Checking session: {}", redisTemplate.opsForValue().get(bookingKey));
        return Objects.equals(redisTemplate.opsForValue().get(bookingKey), "active");
    }

    public void deleteSession(String sessionId, int eventId) {
        String bookingKey = BOOKING_KEY_PREFIX + eventId + ':' + sessionId;
        String bookingCounterKey = BOOKING_COUNTER_KEY + eventId;
        String waitingKey = WAITING_KEY_PREFIX + eventId;
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
            stringRedisConn.del(bookingKey);
            stringRedisConn.decr(bookingCounterKey);
            return null;
        });
        String nextSession = popOldestFromQueue(waitingKey);
        if (nextSession != null) {
            String nextBookingKey = BOOKING_KEY_PREFIX + eventId + ':' + nextSession;
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                stringRedisConnection.incr(bookingCounterKey);
                stringRedisConnection.setEx(nextBookingKey, 60, "active");
                return null;
            });

            log.info("Moved session {} from waiting queue to active booking for event {}", nextSession, eventId);
        }
    }

    public String popOldestFromQueue(String key) {
        Set<String> result = redisTemplate.opsForZSet().range(key, 0, 0);
        if (result != null && !result.isEmpty()) {
            String oldest = result.iterator().next();
            redisTemplate.opsForZSet().remove(key, oldest);
            return oldest;
        }
        return null;
    }
}
