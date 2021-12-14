package com.javamentor.qa.platform.webapp.configs.initializer;

import com.javamentor.qa.platform.service.impl.TestDataInitService;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingClass({"org.springframework.boot.test.context.SpringBootTest"})
public class TestEntityInit implements CommandLineRunner {

    private final TestDataInitService testDataInitService;

    @Autowired
    public TestEntityInit(TestDataInitService testDataInitService) {
        this.testDataInitService = testDataInitService;
    }

//    @Bean
////    @Before
//    public FlywayMigrationStrategy clean() {
//        return flyway -> {
//            flyway.clean();
//            flyway.migrate();
//            flyway.clean();
//        };
//    }

    @Bean
    @Profile("test")
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        FlywayMigrationStrategy strategy = new FlywayMigrationStrategy() {
            @Override
            public void migrate(Flyway flyway) {
                flyway.clean();
                flyway.migrate();
            }
        };

        return strategy;
    }

    @Override
    public void run(String... args) {

    }
}
