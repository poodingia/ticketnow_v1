package com.ticketnow.order.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.order.domain.OrderItem}
 */
public class EmailItemDTO implements Serializable {
    private Integer ticketId;
    private Integer quantity;
    private Double price;
    private Boolean type;

    public EmailItemDTO(Integer ticketId, Integer quantity, Double price, Boolean type) {
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }
}