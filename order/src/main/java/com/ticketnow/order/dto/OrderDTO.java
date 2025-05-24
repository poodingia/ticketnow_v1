package com.ticketnow.order.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.ticketnow.order.domain.Order}
 */
public record OrderDTO(Integer id, Double price, Double quantity, String userId, String status, String email,
                       String name, String couponCode, String eventTitle, Instant createdAt) implements Serializable {
}