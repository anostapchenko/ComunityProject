package com.javamentor.qa.platform;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@AutoConfigureMockMvc
@DBRider
@EnableTransactionManagement
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(classes = JmApplication.class)
@DBUnit(cacheConnection = false, leakHunter = true,caseSensitiveTableNames = true,allowEmptyFields = true )
public abstract class AbstractClassForTests {

    @Autowired
    public MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

   // Класс конфигурации для тестов
}
