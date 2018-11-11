package com.rbr.reactive.jdbc.example.camel.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

@Slf4j
public class ExceptionRethrower {
    public void process(final Exchange exchange) throws Throwable { // NOSONAR
        Throwable caughtException = (Throwable) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        log.debug("Rethrowing Exception " + caughtException.getClass());
        throw caughtException;
    }
}
