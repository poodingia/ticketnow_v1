package com.ticketnow.account.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.account.domain.Feedback}
 */
public record FeedbackCreateDTO( String topic, String message) implements Serializable {
}