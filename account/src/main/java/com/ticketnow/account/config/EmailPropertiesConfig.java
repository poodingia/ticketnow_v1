package com.ticketnow.account.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public record EmailPropertiesConfig(String username, String password) {

}
