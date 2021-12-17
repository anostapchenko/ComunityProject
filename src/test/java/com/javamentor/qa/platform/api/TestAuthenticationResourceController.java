package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// NOT WORKED
@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class TestAuthenticationResourceController {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldBeForbidden() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/numberofusers")).andExpect(status().isForbidden());
    }

    @Test
    public void shouldBeBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

}