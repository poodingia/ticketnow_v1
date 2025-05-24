package com.ticketnow.account.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.account.domain.FeedbackReply}
 */
public record FeedbackReplyCreateDTO(String message, Long feedbackId) implements Serializable {
}