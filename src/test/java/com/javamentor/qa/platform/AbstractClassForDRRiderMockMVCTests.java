package com.javamentor.qa.platform;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.AuthenticationResponse;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DBRider
@EnableTransactionManagement
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(classes = JmApplication.class)
@DBUnit(cacheConnection = false, leakHunter = true,caseSensitiveTableNames = true,allowEmptyFields = true )
public abstract class AbstractClassForDRRiderMockMVCTests {

    private final String url = "http://localhost:8091/api/auth/token";

    @Autowired
    public MockMvc mockMvc;

    @PersistenceContext
    public EntityManager entityManager;

    // Класс конфигурации для тестов

    public String getToken(String username, String password) {

        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        AuthenticationResponse response = new AuthenticationResponse();
        try {
            response = mapper.readValue(
                    mockMvc.perform(post(url)
                                    .content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn().getResponse().getContentAsString(), AuthenticationResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getToken();
    }
}

