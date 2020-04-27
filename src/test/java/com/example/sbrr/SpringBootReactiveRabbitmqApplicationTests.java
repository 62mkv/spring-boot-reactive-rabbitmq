package com.example.sbrr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.blockhound.BlockHound;

import java.util.Collections;

@SpringBootTest(classes = SpringBootReactiveRabbitmqApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SpringBootReactiveRabbitmqApplicationTests {

    @Container
    private static final RabbitMQContainer rabbitMqContainer = new RabbitMQContainer()
            .withExposedPorts(5672);

    @DynamicPropertySource
    public static void addProperties(DynamicPropertyRegistry registry) {
        registry.add("application.rabbitmq.port", () -> rabbitMqContainer.getMappedPort(5672));
    }

    static {
        BlockHound.install();
    }

    @Autowired
    WebTestClient client;

    @Test
    void simpleFlowTest() {
        client
                .get()
                .uri("/api/hello/?name={name}", Collections.singletonMap("name", "test"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

}
