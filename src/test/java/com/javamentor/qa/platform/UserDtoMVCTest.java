package com.javamentor.qa.platform;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserDtoMVCTest extends AbstractClassForDRRiderMockMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    //С помощью DBRider и файла users.yml создаём одного пользователя
    @DataSet(value = "dataset/AuthenticationResourceController/users.yml", strategy = SeedStrategy.INSERT)
    public void shouldReturnIdUsers() throws Exception {
        this.mockMvc.perform(get("/api/user/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }
}
