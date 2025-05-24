package com.ticketnow.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private final RestClient.Builder restClientBuilder;

    public RestClientConfig(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Bean
    public RestClient getRestClient() {
        return restClientBuilder.build();
    }
}
