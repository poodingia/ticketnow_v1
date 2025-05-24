package com.ticketnow.crud.dto;

import java.io.Serializable;

/**
 * Create DTO for {@link com.ticketnow.crud.domain.Coupon}
 */
public record CouponDTO(int id, String code, int eventId, String category, float value, int quantity) implements Serializable {
}