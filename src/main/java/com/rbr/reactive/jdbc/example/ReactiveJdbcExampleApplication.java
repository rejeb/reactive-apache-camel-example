package com.rbr.reactive.jdbc.example;

import com.rbr.reactive.jdbc.example.camel.service.ImportPersonService;
import com.rbr.reactive.jdbc.example.camel.service.impl.ImportPersonServiceImpl;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static com.rbr.reactive.jdbc.example.camel.utils.ImportPersonEndpoint.START_ENDPOINT;

@SpringBootApplication
public class ReactiveJdbcExampleApplication implements CommandLineRunner {
    @Autowired
    private ProducerTemplate producerTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationCtx = SpringApplication
                .run(ReactiveJdbcExampleApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        producerTemplate.sendBody(START_ENDPOINT, LocalDateTime.now());
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
