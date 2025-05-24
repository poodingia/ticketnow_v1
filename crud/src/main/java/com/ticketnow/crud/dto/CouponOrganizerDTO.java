package com.ticketnow.crud.dto;

public record CouponOrganizerDTO(int id, String code, int eventId, String category, float value, int quantity,int used) {
}
