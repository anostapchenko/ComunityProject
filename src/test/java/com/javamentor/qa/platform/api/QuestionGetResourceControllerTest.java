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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuestionGetResourceControllerTest extends AbstractClassForDRRiderMockMVCTests  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "dataset/QuestionResourceController/questions.yml", strategy = SeedStrategy.INSERT)
    // Получение json по существующему вопросу
    public void getCorrectQuestionDtoByIdTest() throws Exception {
        mockMvc.perform(get("http://localhost:8091/api/user/question/1")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru","test15")))
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
    @DataSet(cleanBefore = true, value = "dataset/QuestionResourceController/questions.yml", strategy = SeedStrategy.INSERT)
    // получение ответа по не существующему в тестовой базе вопросу
    public void getWrongQuestionDtoByIdTest() throws Exception {
        mockMvc.perform(get("http://localhost:8091/api/user/question/2")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}