package com.example.sbrr.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kirill.marchuk on 27.04.2020
 */
@ConfigurationProperties(prefix = "application")
@Getter
public class ApplicationProperties {
    private String exchangeName = "default_exchange";

    private Map<String, String> rabbitmq = new HashMap<>();
}
