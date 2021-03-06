package com.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;
import reactor.util.function.Tuples;

import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;


@SpringBootApplication
@Configuration
@ComponentScan("com.sample.web")
public class ReactorMain {
    public static void main(String[] args) {
        SpringApplication.run(ReactorMain.class, args);
        readFibonacciNumbers();
    }

    public static void readFibonacciNumbers() {
        System.out.println("***************************** ");
        WebClient client = WebClient.create("http://localhost:8080");
        Flux<Long> result = client.get()
                .uri("/fibonacci")
                .retrieve()
                // .onStatus(HttpStatus::isError, x -> Mono.error(new RuntimeException("Invalid Response ")))
                .bodyToFlux(Long.class)
                .timeout(Duration.ofMillis(500), x-> Flux.just(-1L));
                //.onErrorResume( x -> Flux.just(-1L, -2L))
                //.limitRequest(10L);
        result.subscribe( x-> System.out.println(x));
    }
}
