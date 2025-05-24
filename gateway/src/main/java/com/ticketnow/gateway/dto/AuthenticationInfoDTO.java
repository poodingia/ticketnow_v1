package com.ticketnow.gateway.dto;

public record AuthenticationInfoDTO(boolean isAuthenticated, AuthenticatedUserDTO authenticatedUser) {
}
