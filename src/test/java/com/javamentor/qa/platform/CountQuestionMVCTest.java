package com.javamentor.qa.platform;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

public class CountQuestionMVCTest extends AbstractClassForDRRiderMockMVCTests {
    @Autowired
    private MockMvc mockMvc;
    @Test
    //С помощью DBRider и файла users.yml создаём одного пользователя
    @DataSet(value = "dataset/users.yml", strategy = SeedStrategy.INSERT)
    public void shouldReturnCountQuestion() throws Exception {
        this.mockMvc.perform(get("/api/user/question/count")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("2")));
    }
}
