package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class QuestionResourceControllerTest extends AbstractClassForDRRiderMockMVCTests  {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "dataset/questions.yml", strategy = SeedStrategy.INSERT)
    public void test() throws Exception {
        mockMvc.perform(get("/api/user/question/{id}", 1))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.authorId").value("test"))
                .andExpect(jsonPath("$.authorReputation").value("test"))
                .andExpect(jsonPath("$.authorName").value("test"))
                .andExpect(jsonPath("$.authorImage").value("test"))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.viewCount").value("test"))
                .andExpect(jsonPath("$.countAnswer").value("test"))
                .andExpect(jsonPath("$.countValuable").value("test"))
                .andExpect(jsonPath("$.countAnswer").value("test"))
                .andExpect(jsonPath("$.persistDateTime").value("test"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("test"))
                .andExpect(jsonPath("$.listTagDto").value("test"));
    }
}