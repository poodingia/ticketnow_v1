package com.ticketnow.account.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.ticketnow.account.domain.Feedback}
 */
public record FeedbackDTO(Instant createdAt, Instant updatedAt, Long id, String userId, String topic,
                          String message, String status, String username) implements Serializable {
}