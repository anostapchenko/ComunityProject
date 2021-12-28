package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestTagResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Test
    //У пользовател с id = 15 есть игнорируемые тэги
    @DataSet(cleanBefore = true, value = "dataset/tagresourcecontroller/data2.yml", strategy = SeedStrategy.REFRESH )
    public void shouldReturnListIrnoredTag() throws Exception {
        this.mockMvc.perform(get("/api/user/tag/ignored")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class))).
                andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("tag 1"));
    }
}
