package com.rbr.reactive.jdbc.example.camel.utils;

public class ImportPersonEndpoint {
    public static final String START_ENDPOINT = "direct:start";
    public static final String PERSON_ENDPOINT = "direct:person";
    public static final String PERSON_PAR_ENDPOINT = "direct:personPar";
    public static final String PERSISTENCE_ENDPOINT = "direct:persistence";

    private ImportPersonEndpoint() {
    }
}
