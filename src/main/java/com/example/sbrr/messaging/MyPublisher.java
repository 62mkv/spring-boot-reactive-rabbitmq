package com.example.sbrr.messaging;

import com.example.sbrr.config.ApplicationProperties;
import com.rabbitmq.client.AMQP;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import static reactor.rabbitmq.ResourcesSpecification.exchange;

/**
 * Created by kirill.marchuk on 27.04.2020
 */
@Component
public class MyPublisher {
    private static final String EXCHANGE_TYPE = "headers";

    private final Sender sender;
    private final String exchangeName;

    public MyPublisher(Sender sender, ApplicationProperties properties) {
        this.sender = sender;
        this.exchangeName = properties.getExchangeName();
        createResources();
    }

    private void createResources() {
        sender.declare(exchange(exchangeName).type(EXCHANGE_TYPE).durable(true)).block();

    }

    public Mono<Void> publishMessage(String message) {
        return sender
                .send(Mono.just(createMessage(message)))
                .checkpoint("Sender#send");
    }

    private OutboundMessage createMessage(String message) {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .correlationId("ffffffffff")
                .build();

        return new OutboundMessage(exchangeName, "#", properties, message.getBytes());
    }
}
