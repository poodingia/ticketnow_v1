package com.ticketnow.account.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.ticketnow.account.domain.FeedbackReply}
 */
public record FeedbackReplyDTO(Instant createdAt, Instant updatedAt, Long id, String userId, String message,
                               Long feedbackId, String username) implements Serializable {
}