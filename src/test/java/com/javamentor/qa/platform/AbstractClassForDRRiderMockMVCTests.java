package com.javamentor.qa.platform;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.AuthenticationResponse;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    protected AbstractClassForDRRiderMockMVCTests() throws MalformedURLException {
    }

    // Класс конфигурации для тестов
    public String getToken(String username, String password) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode node = mapper.createObjectNode();
//        node.put("username",username);
//        node.put("password",password);
//        String JSON_STRING = node.toString();
//        ObjectMapper mapper = new ObjectMapper();
//        HttpEntity entity = new StringEntity(JSON_STRING,ContentType.APPLICATION_JSON);
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(url);
//        request.setEntity(entity);
//        HttpResponse response = httpClient.execute(request);
//        String responseBody = new BasicResponseHandler().handleResponse(response);
//        ObjectNode nodeToken = new ObjectMapper().readValue(responseBody,ObjectNode.class);
//        AuthenticationResponse bodyWithToken = mapper.readValue(nodeToken,AuthenticationResponse.class);
//        return bodyWithToken.getToken();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        MvcResult result = mockMvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
        AuthenticationResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        return response.getToken();

    }
}
