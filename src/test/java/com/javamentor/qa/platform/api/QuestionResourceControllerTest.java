package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.AuthenticationResponse;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
@DataSet(value = "dataset/QuestionResourceController/questions.yml", strategy = SeedStrategy.INSERT)
class QuestionResourceControllerTest extends AbstractClassForDRRiderMockMVCTests  {


    @Autowired
    private MockMvc mockMvc;

//    public String getTokens(String email) throws Exception {
//        String tokenJS = mockMvc.perform(MockMvcRequestBuilders
//                .post("/api/auth/token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"username\" : \"" + email + "\"," +
//                        " \"password\" : \"password\"}")
//        ).andReturn().getResponse().getContentAsString();
//        return new JSONObject(tokenJS).getString("token");
//    }

    @Test
    public void getQuestionDtoByIdTest() throws Exception {
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
        //                .andExpect(jsonPath("$.listTagDto").value();
    }
}