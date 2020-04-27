package com.example.sbrr.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Properties;

/**
 * Created by kirill.marchuk on 27.04.2020
 */
@Configuration
@Slf4j
public class RabbitmqConfiguration {

    private static final Scheduler SCHEDULER = Schedulers.newElastic("rabbit-pool");

    private final Mono<Connection> connectionPublisher;

    public RabbitmqConfiguration(ApplicationProperties properties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.load(buildPropertiesFromSpringProperties(properties.getRabbitmq()));
        this.connectionPublisher = Mono
                .fromCallable(() -> connectionFactory.newConnection("my-connection"))
                .cache();
    }

    @Bean
    public Sender sender(Mono<Connection> connectionMono) {
        SenderOptions senderOptions = new SenderOptions()
                .connectionMono(connectionMono)
                .resourceManagementScheduler(SCHEDULER);

        return RabbitFlux.createSender(senderOptions);
    }

    @Bean
    Mono<Connection> connectionMono() {
        return connectionPublisher;
    }

    @PreDestroy
    public void close() {
        connectionPublisher.
                doOnSuccess(connection -> {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        log.error("Error when closing connection to RabbitMQ: {}", e.getMessage());
                    }
                }).subscribe();
    }

    private Properties buildPropertiesFromSpringProperties(Map<String, String> rabbitConfig) {
        Properties properties = new Properties();
        final String prefix = "rabbitmq.";
        for (Map.Entry<String, String> entry : rabbitConfig.entrySet()) {
            properties.setProperty(prefix + entry.getKey(), entry.getValue());
        }
        return properties;
    }
}
