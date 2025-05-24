package com.ticketnow.order.dto;

import java.io.Serializable;
import java.util.List;

public class EmailDTO implements Serializable {
    private String paymentRef;
    private String paymentDate;
    private String email;
    private List<EmailItemDTO> emailItemDTOs;
    private String method;
    private Integer eventId;

    public EmailDTO(String paymentRef, List<EmailItemDTO> emailItemDTOs, String paymentDate, String email, String method, Integer eventId) {
        this.paymentRef = paymentRef;
        this.emailItemDTOs = emailItemDTOs;
        this.paymentDate = paymentDate;
        this.email = email;
        this.method = method;
        this.eventId = eventId;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public List<EmailItemDTO> getOrderEmailItemDTOs() {
        return emailItemDTOs;
    }

    public void setOrderEmailItemDTOs(List<EmailItemDTO> orderEmailDTOs) {
        this.emailItemDTOs = orderEmailDTOs;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}