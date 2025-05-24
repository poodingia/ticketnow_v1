package com.ticketnow.crud.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record EmailDTO(String paymentRef, String paymentDate, String location, LocalDateTime eventDate, String email,
                       List<EmailItemDTO> emailItemDTOs, String name, String paymentMethod) implements Serializable {

}
