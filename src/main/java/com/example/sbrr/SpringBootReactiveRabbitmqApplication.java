package com.example.sbrr;

import com.example.sbrr.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class SpringBootReactiveRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactiveRabbitmqApplication.class, args);
    }

}
