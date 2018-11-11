package com.rbr.reactive.jdbc.example.camel.route;

import com.rbr.reactive.jdbc.example.camel.prediacte.ImportPersonPredicate;
import com.rbr.reactive.jdbc.example.camel.processor.SavePersonProcessor;
import com.rbr.reactive.jdbc.example.camel.service.ImportPersonService;
import com.rbr.reactive.jdbc.example.camel.utils.ExceptionRethrower;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.util.UnwrapStreamProcessor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static com.rbr.reactive.jdbc.example.camel.utils.ImportPersonEndpoint.PERSISTENCE_ENDPOINT;
import static com.rbr.reactive.jdbc.example.camel.utils.ImportPersonEndpoint.PERSON_ENDPOINT;
import static com.rbr.reactive.jdbc.example.camel.utils.ImportPersonEndpoint.PERSON_PAR_ENDPOINT;
import static com.rbr.reactive.jdbc.example.camel.utils.ImportPersonEndpoint.START_ENDPOINT;

@Component
public class ImportPersonRouteBuilder extends RouteBuilder {
    private static final String LOG_PACKAGE = ImportPersonRouteBuilder.class.getPackage().getName();

    private final ImportPersonPredicate importPersonPredicate;
    private final ImportPersonService importPersonService;
    private final SavePersonProcessor savePersonProcessor;

    public ImportPersonRouteBuilder(CamelContext camelContext,
            ImportPersonPredicate importPersonPredicate,
            ImportPersonService importPersonService,
            SavePersonProcessor savePersonProcessor) {
        super(camelContext);
        this.importPersonPredicate = importPersonPredicate;
        this.importPersonService = importPersonService;

        this.savePersonProcessor = savePersonProcessor;

    }

    @Override
    public void configure() throws Exception {
        errorHandler(defaultErrorHandler().maximumRedeliveries(3).redeliveryDelay(1000));
        configureImportPersonPredicateRoute();
        configureLoadPersonsRoute();
        configureProcessImportPersonsParallel();
        configurePersistenceRoute();
    }

    private void configureImportPersonPredicateRoute() {
        from(START_ENDPOINT).routeId("ImportPersonPredicateRoute")
                .log(LoggingLevel.INFO, LOG_PACKAGE,
                        "Starting process Import persons at ${date:now:dd/MM/yy HH:mm:ss}")
                .errorHandler(defaultErrorHandler().disableRedelivery())
                .choice()
                .when(importPersonPredicate)
                .log(LoggingLevel.INFO, LOG_PACKAGE, "Start file Import persons  processing")

                .to(PERSON_ENDPOINT)
                .log("Success import all persons.")
                .otherwise().log("No data will be imported").endChoice().end();
    }

    private void configureLoadPersonsRoute() {
        from(PERSON_ENDPOINT).routeId("LoadPersonsRoute")

                .errorHandler(defaultErrorHandler().disableRedelivery())

                .log(LoggingLevel.DEBUG, LOG_PACKAGE, "Import persons with date ${body}")
                .doTry()

                .to(PERSON_PAR_ENDPOINT)

                .log(LoggingLevel.DEBUG, LOG_PACKAGE, "Import persons with date SUCCESS")

                .doCatch(Exception.class)
                .log(LoggingLevel.WARN, LOG_PACKAGE,
                        "Error saving person ${body}")

                .bean(new ExceptionRethrower()).endDoTry().end();
    }

    private void configureProcessImportPersonsParallel() {
        from(PERSON_PAR_ENDPOINT)

                .errorHandler(defaultErrorHandler().disableRedelivery())
                .bean(importPersonService, "persons")
                .process(new UnwrapStreamProcessor())
                .split().body().streaming()
                .executorService(buildExecutor())
                .parallelProcessing()
                .to(PERSISTENCE_ENDPOINT)
                .end();
    }

    private void configurePersistenceRoute() {
        from(PERSISTENCE_ENDPOINT).routeId("PersistenceRoute")
                .errorHandler(defaultErrorHandler().disableRedelivery())
                .log(LoggingLevel.DEBUG, LOG_PACKAGE, "Persist person ${body}").doTry()
                .process(savePersonProcessor)
                .log(LoggingLevel.DEBUG, LOG_PACKAGE, "Persist person ${body} SUCCESS")
                .doCatch(Exception.class)
                .log(LoggingLevel.WARN, LOG_PACKAGE,
                        "Error saving person ${body}")
                .bean(new ExceptionRethrower()).endDoTry().end();
    }

    private ExecutorService buildExecutor() {
        return getContext().getExecutorServiceManager()
                .newThreadPool(this, "Main thread pool", 4, 4);
    }

}
