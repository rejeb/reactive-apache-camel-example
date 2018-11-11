package com.rbr.reactive.jdbc.example.camel.service;

import com.rbr.reactive.jdbc.example.repository.model.Person;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ImportPersonService {

    Flux<Person> persons(LocalDateTime dateTime);
}
