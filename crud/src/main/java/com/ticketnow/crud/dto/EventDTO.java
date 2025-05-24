package com.ticketnow.crud.dto;

import java.time.LocalDateTime;

public record EventDTO(int id, String title, String location, String description,
                       String bgImagePath, LocalDateTime date, int max, LocalDateTime startSaleDate,
                       LocalDateTime endSaleDate, boolean isCanceled, int cityId, boolean isApproved,
                       Integer bookingCapacity, String category) {
}
