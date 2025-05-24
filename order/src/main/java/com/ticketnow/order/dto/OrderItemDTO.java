package com.ticketnow.order.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.order.domain.OrderItem}
 */
public record OrderItemDTO(Integer id, Integer ticketId, Integer quantity, Double price,
                           Integer orderId, boolean type, String title) implements Serializable {
}