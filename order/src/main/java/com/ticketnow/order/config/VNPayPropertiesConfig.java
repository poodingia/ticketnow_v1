package com.ticketnow.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vnpay")
public record VNPayPropertiesConfig(String vnpUrl, String hashSecret, String tmnCode, String returnUrl ) {
}
