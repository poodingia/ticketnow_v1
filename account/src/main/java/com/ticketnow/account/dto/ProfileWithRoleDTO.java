package com.ticketnow.account.dto;

public record ProfileWithRoleDTO (String id, String username, String email, String firstName, String lastName, Boolean isAdmin) {
    public static ProfileWithRoleDTO fromCustomerDTO(CustomerDTO customerDTO, Boolean isAdmin) {
        return new ProfileWithRoleDTO(customerDTO.id(), customerDTO.username(), customerDTO.email(), customerDTO.firstName(), customerDTO.lastName(), isAdmin);
    }
}
