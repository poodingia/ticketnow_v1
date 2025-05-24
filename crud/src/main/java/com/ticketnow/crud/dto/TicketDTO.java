package com.ticketnow.crud.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.crud.domain.Ticket}
 */
public record TicketDTO(Integer id, Boolean status, String ownerId, TicketTypeDTO type,
                        EventDTO event) implements Serializable {
}