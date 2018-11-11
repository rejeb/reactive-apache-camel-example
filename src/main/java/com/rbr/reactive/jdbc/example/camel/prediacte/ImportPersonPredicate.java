package com.rbr.reactive.jdbc.example.camel.prediacte;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class ImportPersonPredicate implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        boolean executeImport = new Random().nextBoolean();
        log.info("Execute batch value is {}.", executeImport);
        return true;
    }
}
