package com.rbr.reactive.jdbc.example.camel.service.impl;

import com.rbr.reactive.jdbc.example.camel.service.ImportPersonService;
import com.rbr.reactive.jdbc.example.repository.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Slf4j
public class ImportPersonServiceImpl implements ImportPersonService {

    private static final String PERSON_RESOURCE = "/persons";
    private final WebClient webClient;
    private final String baseUrl;

    public ImportPersonServiceImpl(WebClient webClient, String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Flux<Person> persons(LocalDateTime dateTime) {
        Flux<Person> flux= webClient.get().uri(baseUrl + PERSON_RESOURCE).retrieve().bodyToFlux(Person.class);
            log.info("get persons from web service.");
        return flux;
    }
}
