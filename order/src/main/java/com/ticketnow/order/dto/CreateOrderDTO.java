package com.ticketnow.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.ticketnow.order.domain.Order}
 */
public record CreateOrderDTO(@NotNull Double price, @NotNull Double quantity, Set<CreateOrderItemDto> orderItems, @Email String email,
                             @NotNull String name, String couponCode, @NotNull Integer eventId, @NotNull String paymentMethod) implements Serializable {
    /**
     * DTO for {@link com.ticketnow.order.domain.OrderItem}
     */
    public record CreateOrderItemDto(Integer ticketId, Integer quantity, Double price, boolean isType) implements Serializable {
    }
}