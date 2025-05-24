package com.ticketnow.order.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ticketnow.order.domain.Order}
 */
public class OrderPaymentDTO implements Serializable {
    private Integer id;
    private String vnpUrl;

    public OrderPaymentDTO(Integer id, String vnpUrl) {
        this.id = id;
        this.vnpUrl = vnpUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getvnpUrl() {
        return vnpUrl;
    }

    public void setvnpUrl(String vnpUrl) {
        this.vnpUrl = vnpUrl;
    }
}