package com.ticketnow.crud.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.ticketnow.crud.domain.Event}
 */
public record CreateEventDTO(EventDTO event,
                             Set<TicketTypeDto> ticketTypes) implements Serializable {
    /**
     * DTO for {@link com.ticketnow.crud.domain.TicketType}
     */
    public record TicketTypeDto(String description, Float price, Integer quantity, boolean directly) implements Serializable {
    }
}