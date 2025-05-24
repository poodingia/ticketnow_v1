package com.ticketnow.crud.dto;

import java.io.Serializable;

public class OrderEmailItemDTO implements Serializable {
    private Integer ticketId;
    private Integer quantity;
    private Double price;
    private Boolean type;

    public OrderEmailItemDTO(Integer ticketId, Integer quantity, Double price, Boolean type) {
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public OrderEmailItemDTO() {

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