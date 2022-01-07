package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestUserResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Test
    //Вывод Dto по id
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/testUserResourceController/roles.yml",
                    "dataset/testUserResourceController/users.yml",
                    "dataset/testUserResourceController/reputacion.yml",
                    "dataset/testUserResourceController/answers.yml",
                    "dataset/testUserResourceController/questions.yml"
            },
            strategy = SeedStrategy.REFRESH )
    public void getApiUserDtoId() throws Exception {
        this.mockMvc.perform(get("/api/user/102")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("102"))
                .andExpect(jsonPath("$.email").value("user102@mail.ru"))
                .andExpect(jsonPath("$.fullName").value("test 15"))
                .andExpect(jsonPath("$.imageLink").value("photo"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.reputation").value(100));
    }
    //Проверяем на не существующий id
    @Test
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/testUserResourceController/roles.yml",
                    "dataset/testUserResourceController/users.yml",
                    "dataset/testUserResourceController/reputacion.yml",
                    "dataset/testUserResourceController/answers.yml",
                    "dataset/testUserResourceController/questions.yml"
            },
            strategy = SeedStrategy.REFRESH )
    public void getNotUserDtoId() throws Exception {
        this.mockMvc.perform(get("/api/user/105")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
//  Получаем всех пользователей из БД
    @DataSet(cleanBefore = true,
    value = {
            "dataset/userresourcecontroller/roles.yml",
            "dataset/userresourcecontroller/users.yml",
            "dataset/userresourcecontroller/questions.yml",
            "dataset/userresourcecontroller/reputations.yml"
    },
    strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllUsers() throws Exception{
        this.mockMvc.perform(get("/api/user/new?page=1")
                        .contentType("application/json")
                        .header("Authorization","Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.totalResultCount").value("3"))
                .andExpect(jsonPath("$.items[0].id").value("101"))
                .andExpect(jsonPath("$.items[0].email").value("test15@mail.ru"))
                .andExpect(jsonPath("$.items[0].fullName").value("test 101"))
                .andExpect(jsonPath("$.items[0].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[0].city").value("Moscow"))
                .andExpect(jsonPath("$.items[0].reputation").value("100"))
                .andExpect(jsonPath("$.items[1].id").value("102"))
                .andExpect(jsonPath("$.items[1].email").value("test102@mail.ru"))
                .andExpect(jsonPath("$.items[1].fullName").value("test 102"))
                .andExpect(jsonPath("$.items[1].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[1].city").value("Moscow"))
                .andExpect(jsonPath("$.items[1].reputation").value("500"))
                .andExpect(jsonPath("$.items[2].id").value("103"))
                .andExpect(jsonPath("$.items[2].email").value("test103@mail.ru"))
                .andExpect(jsonPath("$.items[2].fullName").value("test 103"))
                .andExpect(jsonPath("$.items[2].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[2].city").value("Moscow"))
                .andExpect(jsonPath("$.items[2].reputation").value("80"))
                .andExpect(jsonPath("$.itemsOnPage").value("3"))
                ;

    }

    @Test
//  Получаем всех пользователей из БД (у пользователей еще нет репутации)
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users.yml",
                    "dataset/userresourcecontroller/questions.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllUsersWithNullReputaion() throws Exception{
        this.mockMvc.perform(get("/api/user/new?page=1")
                        .contentType("application/json")
                        .header("Authorization","Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.totalResultCount").value("3"))
                .andExpect(jsonPath("$.items[0].id").value("101"))
                .andExpect(jsonPath("$.items[0].email").value("test15@mail.ru"))
                .andExpect(jsonPath("$.items[0].fullName").value("test 101"))
                .andExpect(jsonPath("$.items[0].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[0].city").value("Moscow"))
                .andExpect(jsonPath("$.items[0].reputation").value(nullValue()))
                .andExpect(jsonPath("$.items[1].id").value("102"))
                .andExpect(jsonPath("$.items[1].email").value("test102@mail.ru"))
                .andExpect(jsonPath("$.items[1].fullName").value("test 102"))
                .andExpect(jsonPath("$.items[1].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[1].city").value("Moscow"))
                .andExpect(jsonPath("$.items[1].reputation").value(nullValue()))
                .andExpect(jsonPath("$.items[2].id").value("103"))
                .andExpect(jsonPath("$.items[2].email").value("test103@mail.ru"))
                .andExpect(jsonPath("$.items[2].fullName").value("test 103"))
                .andExpect(jsonPath("$.items[2].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[2].city").value("Moscow"))
                .andExpect(jsonPath("$.items[2].reputation").value(nullValue()))
                .andExpect(jsonPath("$.itemsOnPage").value("3"))
        ;

    }
    @Test
//  Получаем всех пользователей из БД, кроме пользователя с флагом is_deleted: true
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users_with_deleted_user.yml",
                    "dataset/userresourcecontroller/questions.yml",
                    "dataset/userresourcecontroller/reputations.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllUsersWithoutDeletedUser() throws Exception{
        this.mockMvc.perform(get("/api/user/new?page=1")
                        .contentType("application/json")
                        .header("Authorization","Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.totalResultCount").value("3"))
                .andExpect(jsonPath("$.items[0].id").value("101"))
                .andExpect(jsonPath("$.items[0].email").value("test15@mail.ru"))
                .andExpect(jsonPath("$.items[0].fullName").value("test 101"))
                .andExpect(jsonPath("$.items[0].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[0].city").value("Moscow"))
                .andExpect(jsonPath("$.items[0].reputation").value("100"))
                .andExpect(jsonPath("$.items[1].id").value("103"))
                .andExpect(jsonPath("$.items[1].email").value("test103@mail.ru"))
                .andExpect(jsonPath("$.items[1].fullName").value("test 103"))
                .andExpect(jsonPath("$.items[1].imageLink").value("photo"))
                .andExpect(jsonPath("$.items[1].city").value("Moscow"))
                .andExpect(jsonPath("$.items[1].reputation").value("80"))
                .andExpect(jsonPath("$.itemsOnPage").value("2"))
        ;

    }
}
