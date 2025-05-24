package com.ticketnow.account.dto;

import java.io.Serializable;

public record CrudEmailItemDTO(Integer quantity, Double price, String description) implements Serializable {

}
