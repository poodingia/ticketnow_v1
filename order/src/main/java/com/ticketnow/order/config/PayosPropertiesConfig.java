package com.ticketnow.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "payos")
public record PayosPropertiesConfig(String clientId, String apiKey, String checksum) {

}
