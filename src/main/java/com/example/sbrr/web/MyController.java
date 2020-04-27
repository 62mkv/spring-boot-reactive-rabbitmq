package com.example.sbrr.web;

import com.example.sbrr.messaging.MyPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 * Created by kirill.marchuk on 27.04.2020
 */
@Controller
public class MyController {

    private final MyPublisher publisher;

    public MyController(MyPublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping(produces = "application/json", value = "/api/hello/")
    public Mono<ResponseEntity> hello(@RequestParam("name") String name) {
        return publisher.publishMessage("Hello, ".concat(name))
                .thenReturn(ResponseEntity.ok().build());
    }

}
