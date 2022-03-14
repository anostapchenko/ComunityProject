package com.javamentor.qa.platform.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class TestGlobalSearchResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Test
    @DataSet(
            value = {
                    "dataset/testQuestionResourceController/question_different_date.yml",
                    "dataset/testQuestionResourceController/tag.yml",
                    "dataset/testQuestionResourceController/questions_has_tag1.yml",
                    "dataset/QuestionResourceController/users.yml",
                    "dataset/testQuestionResourceController/role.yml",
                    "dataset/QuestionResourceController/votes_on_questions.yml"
            }
    )
    public void getQuestionGlobalSearchByTagsName() throws Exception {

        mockMvc.perform(get("/api/search/tagged/testNameTag2")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(2))
                .andExpect(jsonPath("$.items.length()").value(1));
    }
}
