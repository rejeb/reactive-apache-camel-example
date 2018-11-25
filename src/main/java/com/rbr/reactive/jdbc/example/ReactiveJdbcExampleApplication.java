package com.rbr.reactive.jdbc.example;

import com.rbr.reactive.jdbc.example.camel.service.ImportPersonService;
import com.rbr.reactive.jdbc.example.camel.service.impl.ImportPersonServiceImpl;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static com.rbr.reactive.jdbc.example.camel.utils.ImportPersonEndpoint.START_ENDPOINT;

@SpringBootApplication
public class ReactiveJdbcExampleApplication implements CommandLineRunner {

    /** The command line options. */
    private static final Options OPTIONS = new Options();
    /** The help command line option. */
    private static final String HELP_OPTION = "h";
    private static final String HELP_OPTION_LONG = "help";

    /** The daemon command line option. */
    private static final String CURRENT_DATE_OPTION = "d";
    private static final String CURRENT_DATE_OPTION_LONG = "current-date";
    static {
        OPTIONS.addOption(HELP_OPTION, HELP_OPTION_LONG, false, "Command line help");
        OPTIONS.addOption(CURRENT_DATE_OPTION, CURRENT_DATE_OPTION_LONG, true, "Start apache camel with current date value");
    }

    @Autowired
    private ProducerTemplate producerTemplate;

    public static void main(String[] args) throws ParseException {

        final CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(OPTIONS, args, true);
        if (commandLine.hasOption(HELP_OPTION)) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("camel-batch", OPTIONS, true);
            return;
        }
        SpringApplication
                .run(ReactiveJdbcExampleApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        final CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(OPTIONS, args, true);

        LocalDateTime currentDate=LocalDateTime.parse(commandLine.getOptionValue(CURRENT_DATE_OPTION));
        producerTemplate.sendBody(START_ENDPOINT, currentDate);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public ImportPersonService importPersonService(@Value("${import.person.url}") String baseUrl) {
        return new ImportPersonServiceImpl(webClient(), baseUrl);
    }
}
