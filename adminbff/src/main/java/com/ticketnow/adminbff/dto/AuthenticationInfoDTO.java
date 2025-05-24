package com.ticketnow.adminbff.dto;

public record AuthenticationInfoDTO(boolean isAuthenticated, AuthenticatedUserDTO authenticatedUser) {
}
