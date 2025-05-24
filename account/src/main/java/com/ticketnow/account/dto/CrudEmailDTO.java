package com.ticketnow.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record CrudEmailDTO(String paymentRef, String paymentDate, String location, LocalDateTime eventDate,
                           String email,
                           @JsonProperty("emailItemDTOs") List<CrudEmailItemDTO> crudEmailItemDTOs, String name, String paymentMethod) implements Serializable {

}
