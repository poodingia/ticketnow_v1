package com.ticketnow.crud.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.ticketnow.crud.domain.Event}
 */
public record EventOrganizerDTO(Integer id, String title, String location, String description, String bgImagePath,
                                LocalDateTime date, Integer max, Boolean isApproved) implements Serializable {
}