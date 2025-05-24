package com.ticketnow.crud.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.crud.domain.TicketType}
 */
public record TicketTypeDTO(
        Integer id,
        String description,
        Float price,
        Integer quantity,
        Integer eventId,
        int reservedQuantity) implements Serializable {
}