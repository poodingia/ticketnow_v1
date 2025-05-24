package com.ticketnow.crud.dto;

import java.io.Serializable;

public record EmailItemDTO(Integer quantity, Double price, String description) implements Serializable {

}
