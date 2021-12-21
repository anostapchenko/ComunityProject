package com.javamentor.qa.platform;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BRRiderMockMVCTest extends AbstractClassForDRRiderMockMVCTests {

    @Autowired
    private MockMvc mockMvc;

    protected BRRiderMockMVCTest() {
    }

    //Проверка количества пользователей в таблице User_Entity.
    @Test
    //С помощью DBRider и файла data.yml создаём одного пользователя
    @DataSet(value = "dataset/users.yml", strategy = SeedStrategy.INSERT)
    public void shouldReturnNumberOfUsers() throws Exception {
        this.mockMvc.perform(get("/api/numberofusers")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }
}


