package com.ticketnow.search.dto;

public record EventSearchDTO(Long id, String title, String bgImagePath, String description, String date, String category, String location, Boolean approved) {
}
