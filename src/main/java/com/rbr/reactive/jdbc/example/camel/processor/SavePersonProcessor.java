package com.rbr.reactive.jdbc.example.camel.processor;

import com.rbr.reactive.jdbc.example.repository.PersonRepository;
import com.rbr.reactive.jdbc.example.repository.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SavePersonProcessor implements Processor {

    private final PersonRepository personRepository;

    public SavePersonProcessor(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void process(Exchange exchange) {
        Person person = exchange.getIn().getBody(Person.class);
        log.info("save person : {}",person);
            personRepository.save(person);

    }
}
