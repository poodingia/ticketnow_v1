package com.ticketnow.gateway.resource;

import com.ticketnow.gateway.dto.AuthenticatedUserDTO;
import com.ticketnow.gateway.dto.AuthenticationInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationResource {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationResource.class);

    @GetMapping("/authentication")
    public ResponseEntity<AuthenticationInfoDTO> user(@AuthenticationPrincipal OAuth2User principal) {
        logger.info("User authenticated: {}", principal);
        if (principal == null) {
            return ResponseEntity.ok(new AuthenticationInfoDTO(false, null));
        }
        AuthenticatedUserDTO authenticatedUser = new AuthenticatedUserDTO(principal.getAttribute("preferred_username"));
        return ResponseEntity.ok(new AuthenticationInfoDTO(true, authenticatedUser));
    }
}
