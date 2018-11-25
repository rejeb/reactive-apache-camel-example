package com.rbr.reactive.jdbc.example;

import com.rbr.reactive.jdbc.example.repository.PersonRepository;
import com.rbr.reactive.jdbc.example.repository.model.Person;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ReactiveJdbcExampleApplication.class})
@AutoConfigureWireMock(port = 0,files = "classpath:stubs")
@BootstrapWith(CamelIntegrationTest.Bootstrapper.class)
public class CamelIntegrationTest {

    static class Bootstrapper extends SpringBootTestContextBootstrapper {
        static class ArgumentSupplyingContextLoader extends SpringBootContextLoader {
            @Override
            protected SpringApplication getSpringApplication() {
                return new SpringApplication() {
                    @Override
                    public ConfigurableApplicationContext run(String... args) {
                        return super.run("-d2018-01-01T00:00:00");
                    }
                };
            }
        }

        @Override
        protected Class<? extends ContextLoader> getDefaultContextLoaderClass(Class<?> testClass) {
            return ArgumentSupplyingContextLoader.class;
        }
    }


    @Autowired
    private CamelContext camelContext;

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testImport(){
        exit.checkAssertionAfterwards(() -> {
            List<Person> rows = personRepository.findAll();
            assertNotNull(rows.size());
          });
    }

    @After
    public void finalize() throws Exception {
        camelContext.stop();
    }

}
