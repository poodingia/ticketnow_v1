package com.ticketnow.order.dto;

import com.ticketnow.order.domain.Status;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.ticketnow.order.domain.Order}
 */
public class OrderDetailDTO implements Serializable {
    private Integer id;
    private Double price;
    private Double quantity;
    private String userId;
    private Status status;
    private Set<OrderItemDto> orderItems;
    private String email;
    private String name;

    public OrderDetailDTO(Integer id, Double price, Double quantity, String userId, Status status, Set<OrderItemDto> orderItems, String name, String email) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
        this.status = status;
        this.orderItems = orderItems;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * DTO for {@link com.ticketnow.order.domain.OrderItem}
     */
    public static class OrderItemDto implements Serializable {
        private Integer id;
        private Integer ticketId;
        private Integer quantity;
        private Double price;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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
    }
}