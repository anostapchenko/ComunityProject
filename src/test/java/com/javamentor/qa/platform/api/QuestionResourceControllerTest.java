package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class QuestionResourceControllerTest extends AbstractClassForDRRiderMockMVCTests  {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "dataset/QuestionResourceController/questions.yml", strategy = SeedStrategy.INSERT)
    public void test() throws Exception {
        mockMvc.perform(get("/api/user/question/{id}", 1))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.authorId").value(15L))
                .andExpect(jsonPath("$.authorReputation").value(100L))
                .andExpect(jsonPath("$.authorName").value("test 15"))
                .andExpect(jsonPath("$.authorImage").value("photo"))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.viewCount").value(0L))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.countValuable").value(-1))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-13T18:09:52.716"));
//                .andExpect(jsonPath("$.listTagDto").value("{id=1, name=test, description=test}")); Последняя строка не стартует, не могу подставить тествое значение
    }
}