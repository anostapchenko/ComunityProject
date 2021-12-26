package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.AuthenticationResponse;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
class QuestionGetResourceControllerTest extends AbstractClassForDRRiderMockMVCTests  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "dataset/QuestionResourceController/questions.yml", strategy = SeedStrategy.INSERT)
    public void getCorrectQuestionDtoByIdTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("test15@mail.ru");
        request.setPassword("test15");
        MvcResult result = mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        AuthenticationResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        mockMvc.perform(get("http://localhost:8091/api/user/question/1").header("Authorization", "Bearer " + response.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.authorId").value(15))
                .andExpect(jsonPath("$.authorReputation").value(100))
                .andExpect(jsonPath("$.authorName").value("test 15"))
                .andExpect(jsonPath("$.authorImage").value("photo"))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.viewCount").value(0L))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.countValuable").value(-1))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.listTagDto[0].description").value("testDescriptionTag"))
                .andExpect(jsonPath("$.listTagDto[0].name").value("testNameTag"))
                .andExpect(jsonPath("$.listTagDto[0].id").value(1));
    }
    @Test
    public void getWrongQuestionDtoByIdTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("test15@mail.ru");
        request.setPassword("test15");
        MvcResult result = mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        AuthenticationResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        mockMvc.perform(get("http://localhost:8091/api/user/question/2").header("Authorization", "Bearer " + response.getToken()))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}