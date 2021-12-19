package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.AuthenticationResponse;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.JwtUtil;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestAuthenticationResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Test
    public void forbiddenWhenNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/numberofusers"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DataSet(value = "dataset/users.yml", strategy = SeedStrategy.INSERT)
    public void getTokenAndPassRequestForValidUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = userService.getByEmail("test15@mail.ru").get();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        MvcResult result = mockMvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        AuthenticationResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(), AuthenticationResponse.class);

        mockMvc.perform(get("/api/numberofusers").header("Authorization", "Bearer " + response.getToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void badRequestWhenUserNotExist() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("notexisted@mail.ru");
        request.setPassword("password");
        mockMvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "dataset/users.yml", strategy = SeedStrategy.INSERT)
    public void forbiddenWhenTokenExpired() throws Exception {

        User user = userService.getByEmail("test15@mail.ru").get();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        String token = jwtUtil.generateToken(user, -1L);

        mockMvc.perform(get("/api/numberofusers").header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}