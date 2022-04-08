package com.javamentor.qa.platform.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUserResourceController extends AbstractClassForDRRiderMockMVCTests {

    private static final String USERNAME = "user100@mail.ru";
    private static final String PASSWORD = "password";
    private static final String URL_VOTE = "/api/user/vote?";

    @Test
    //Вывод Dto по id
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/testUserResourceController/roles.yml",
                    "dataset/testUserResourceController/users.yml",
                    "dataset/testUserResourceController/reputation.yml",
                    "dataset/testUserResourceController/answers.yml",
                    "dataset/testUserResourceController/questions.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void getApiUserDtoId() throws Exception {
        this.mockMvc.perform(get("/api/user/102")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("user102@mail.ru", "test15")))
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
                    "dataset/testUserResourceController/reputation.yml",
                    "dataset/testUserResourceController/answers.yml",
                    "dataset/testUserResourceController/questions.yml"
            },
            strategy = SeedStrategy.REFRESH)
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
    public void shouldReturnAllUsers() throws Exception {
        this.mockMvc.perform(get("/api/user/new?page=1")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test15")))
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
                .andExpect(jsonPath("$.items[2].reputation").value("800"))
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
    public void shouldReturnAllUsersWithNullReputaion() throws Exception {
        this.mockMvc.perform(get("/api/user/new?page=1")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test15")))
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
    public void shouldReturnAllUsersWithoutDeletedUser() throws Exception {
        this.mockMvc.perform(get("/api/user/new?page=1")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test15")))
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
                .andExpect(jsonPath("$.items[1].reputation").value("800"))
                .andExpect(jsonPath("$.itemsOnPage").value("2"));
    }

    @Test
    @DataSet(value = {
            "dataset/testUserResourceController/roleUser.yml",
            "dataset/testUserResourceController/users3.yml",
            "dataset/testUserResourceController/repEmpty.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void ifReputationEmptyItems2() throws Exception {
        mockMvc.perform(
                get(URL_VOTE + "page=1&items=2")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[1].id").value(101));
        mockMvc.perform(
                get(URL_VOTE + "page=2&items=2")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(102));
    }

    @Test
    @DataSet(value = {
            "dataset/testUserResourceController/roleUser.yml",
            "dataset/testUserResourceController/users3.yml",
            "dataset/testUserResourceController/repUnnecessary.yml",
    },
            tableOrdering = {
                    "role",
                    "user_entity",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void ifNotNecessaryVotesItems2() throws Exception {
        mockMvc.perform(
                get(URL_VOTE + "page=1&items=2")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[1].id").value(101));
        mockMvc.perform(
                get(URL_VOTE + "page=2&items=2")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(102));
    }

    @Test
    @DataSet(value = {
            "dataset/testUserResourceController/roleUser.yml",
            "dataset/testUserResourceController/users20.yml",
            "dataset/testUserResourceController/repUnnecessary.yml",
    },
            tableOrdering = {
                    "role",
                    "user_entity",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void ifItemsNullThenArraySize10() throws Exception {
        mockMvc.perform(
                get(URL_VOTE + "page=1")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(10));
    }

    @Test
    @DataSet(value = {
            "dataset/testUserResourceController/roleUser.yml",
            "dataset/testUserResourceController/users20.yml",
            "dataset/testUserResourceController/repFirst3DownVoteAndLast3UpVote.yml",
    },
            tableOrdering = {
                    "role",
                    "user_entity",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void ifFirst3DownVoteAndLast3UpVote() throws Exception {
        mockMvc.perform(
                get(URL_VOTE + "page=1&items=10")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(10))
                .andExpect(jsonPath("$.items[0].id").value(117))
                .andExpect(jsonPath("$.items[1].id").value(118))
                .andExpect(jsonPath("$.items[2].id").value(119));
        mockMvc.perform(
                get(URL_VOTE + "page=2&items=10")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(10))
                .andExpect(jsonPath("$.items[7].id").value(100))
                .andExpect(jsonPath("$.items[8].id").value(101))
                .andExpect(jsonPath("$.items[9].id").value(102));
    }

    @Test
    @DataSet(value = {
            "dataset/testUserResourceController/roleUser.yml",
            "dataset/testUserResourceController/users3.yml",
            "dataset/testUserResourceController/repCount15.yml",
    },
            tableOrdering = {
                    "role",
                    "user_entity",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void ifReputationCount15() throws Exception {
        mockMvc.perform(
                get(URL_VOTE + "page=1")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3))
                .andExpect(jsonPath("$.items[0].reputation").value(15));
    }

    @Test
    @DataSet(value = {
            "dataset/testUserResourceController/roleUser.yml",
            "dataset/testUserResourceController/users3.yml"
    },
            tableOrdering = {
                    "role",
                    "user_entity"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void ifCurrentPageIncorrectThen400() throws Exception {
        mockMvc.perform(
                get(URL_VOTE + "page=")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
//  Проверяем изменение пароля
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users_with_deleted_user.yml",
                    "dataset/userresourcecontroller/questions.yml",
                    "dataset/userresourcecontroller/reputations.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnUserWithChangedPassword() throws Exception {
//                          Ставим новый пароль
        this.mockMvc.perform(patch("/api/user/change/password?password=test534")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isOk())
        ;
//                          Заходим под новым паролем
        this.mockMvc.perform(patch("/api/user/change/password?password=anotherTest534")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test534")))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
//  Получаем всеx User из БД отсортированных по репутации c аттрибутом isDeleted=true
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users_with_deleted_user.yml",
                    "dataset/userresourcecontroller/questions.yml",
                    "dataset/userresourcecontroller/reputations.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllUsersSortByRepDelTrue() throws Exception {
        // указаны параметры page и items

        String MyToken = mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\" : \"test15@mail.ru\"," +
                        " \"password\" : \"test15\"}")
        ).andReturn().getResponse().getContentAsString();

        MyToken = MyToken.substring(MyToken.indexOf(":") + 2, MyToken.length() - 2);

        this.mockMvc.perform(get("/api/user/reputation?page=1&items=3")
                .contentType("application/json")
                .header("Authorization", "Bearer " + MyToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.itemsOnPage").value("2"))  //ожидаем 2, так как аттрибут is_deleted: true у user с id=102
                .andExpect(jsonPath("$.totalResultCount").value("3"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(103, 101)));


        // нет обязательного параметра - page
        mockMvc.perform(get("/api/user/reputation?items=3")
                .header("Authorization", "Bearer " + MyToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
//  Получаем всеx User из БД отсортированных по репутации.
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users10.yml",
                    "dataset/userresourcecontroller/questions.yml",
                    "dataset/userresourcecontroller/reputations10.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllUsersSortByRep() throws Exception {

        String MyToken = mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\" : \"test15@mail.ru\"," +
                        " \"password\" : \"test15\"}")
        ).andReturn().getResponse().getContentAsString();

        MyToken = MyToken.substring(MyToken.indexOf(":") + 2, MyToken.length() - 2);

        this.mockMvc.perform(get("/api/user/reputation?page=1&items=10")
                .contentType("application/json")
                .header("Authorization", "Bearer " + MyToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"))
                .andExpect(jsonPath("$.totalResultCount").value("10"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(110, 107, 104, 106, 103, 109, 108, 102, 101, 105)));
    }

    @Test
//  Получаем всеx User из БД отсортированных по репутации, допуская что 2 rep может быть у одного юзера .
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users10.yml",
                    "dataset/userresourcecontroller/questions.yml",
                    "dataset/userresourcecontroller/reputations101.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllUsersSortByRepNull() throws Exception {

        String MyToken = mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\" : \"test15@mail.ru\"," +
                        " \"password\" : \"test15\"}")
        ).andReturn().getResponse().getContentAsString();

        MyToken = MyToken.substring(MyToken.indexOf(":") + 2, MyToken.length() - 2);

        this.mockMvc.perform(get("/api/user/reputation?page=1&items=10")
                .contentType("application/json")
                .header("Authorization", "Bearer " + MyToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"))
                .andExpect(jsonPath("$.totalResultCount").value("10"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(110, 107, 104, 106, 103, 109, 101, 108, 105)));
    }

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    @Test
    @DataSet(cleanBefore = true, cleanAfter = true,
            value = {
                    "dataset/testUserResourceController/shouldCacheIsUserExistByEmail/roles.yml",
                    "dataset/testUserResourceController/shouldCacheIsUserExistByEmail/users.yml"
                    },
            strategy = SeedStrategy.CLEAN_INSERT)
    public void shouldCacheIsUserExistByEmail() throws Exception {

        String token101 = "Bearer " + getToken("test102@mail.ru", "user1");

        //Проверяю кэширование существующего
        assertNull(cacheManager.getCache("User").get("test102@mail.ru"));
        userDao.isUserExistByEmail("test102@mail.ru");
        assertNotNull(cacheManager.getCache("User").get("test102@mail.ru"));
        assertTrue(userDao.isUserExistByEmail("test102@mail.ru"));

        //Проверяю кэширование несуществующего
        assertNull(cacheManager.getCache("User").get("test100@mail.ru"));
        userDao.isUserExistByEmail("test100@mail.ru");
        assertNotNull(cacheManager.getCache("User").get("test100@mail.ru"));
        assertFalse(userDao.isUserExistByEmail("test100@mail.ru"));

        //Меняю пароль
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/user/change/password?password=newPassword321")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token101))
                .andDo(print())
                .andExpect(status().isOk());

        assertNull(cacheManager.getCache("User").get("test102@mail.ru"));

        userDao.isUserExistByEmail("test102@mail.ru");
        assertNotNull(cacheManager.getCache("User").get("test102@mail.ru"));

        //Удаляю по email
        userService.deleteById("test102@mail.ru");
        assertNull(cacheManager.getCache("User").get("test102@mail.ru"));

        //Удаляю по id с помощью Dao - ничего не должен удалять
        userDao.deleteById(103L);
        assertTrue(userService.getById(103L).isPresent());
    }

    @Test
    @DataSet(cleanBefore = true, cleanAfter = true,
            value = {
                    "dataset/testUserResourceController/shouldCacheIsUserExistByEmail/roles.yml",
                    "dataset/testUserResourceController/shouldCacheIsUserExistByEmail/users.yml"
            },
            strategy = SeedStrategy.CLEAN_INSERT)
    public void shouldCacheUserAfterLogin() throws Exception {

        //Проверяю кэширование до логина
        assertNull(cacheManager.getCache("User").get("test102@mail.ru"));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("user1");
        authenticationRequest.setUsername("test102@mail.ru");

        mockMvc.perform(post("/api/auth/token")
                        .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //Проверяю кэширование после логина
        assertNotNull(cacheManager.getCache("User").get("test102@mail.ru"));

        //Удаляю и проверяю кэширование
        userService.deleteById("test102@mail.ru");
        assertNull(cacheManager.getCache("User").get("test102@mail.ru"));

        //Обновляю и проверяю кэширование
        userService.update(userService.getByEmail("test102@mail.ru").get());
        assertNull(cacheManager.getCache("User").get("test102@mail.ru"));
    }
    @Test
    @DataSet(cleanBefore = true, cleanAfter = true,
            value = {
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/userresourcecontroller/users.yml",
                    "dataset/testUserResourceController/testGetAllUserProfileQuestionDtoIsDelete/questions.yml",
                    "dataset/testUserResourceController/testGetAllUserProfileQuestionDtoIsDelete/tags.yml",
                    "dataset/testUserResourceController/testGetAllUserProfileQuestionDtoIsDelete/questions_has_tag.yml",
                    "dataset/testUserResourceController/testGetAllUserProfileQuestionDtoIsDelete/answers.yml"
            },
            strategy = SeedStrategy.CLEAN_INSERT)
    public void testGetAllUserProfileQuestionDtoIsDelete() throws Exception {

        String USER_TOKEN_USERID_102 = "Bearer " + getToken("test102@mail.ru", "test15");
        mockMvc.perform(get("/api/user/profile/delete/questions")
                        .header(AUTHORIZATION, USER_TOKEN_USERID_102)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        String USER_TOKEN_USERID_101 = "Bearer " + getToken("test15@mail.ru", "test15");
        mockMvc.perform(get("/api/user/profile/delete/questions")
                        .header(AUTHORIZATION, USER_TOKEN_USERID_101)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[*].questionId").value(containsInRelativeOrder(103,105,106)))
                .andExpect(jsonPath("$.[*].listTagDto.length()").value(containsInRelativeOrder(2,0,3)))
                .andExpect(jsonPath("$.[*].countAnswer").value(containsInRelativeOrder(3,1,0)));
    }

    @Test
    @DataSet(cleanBefore = true, cleanAfter = true,
            value = {
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/roles.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/users.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/questions.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/tags.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/questions_has_tag.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/answers.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/votes_on_questions.yml",
                    "dataset/testUserResourceController/testGetAllBookMarksInUserProfile/question_viewed.yml"
            },
            strategy = SeedStrategy.CLEAN_INSERT)

    public void testGetAllBookMarksInUserProfile() throws Exception {
        String USER_TOKEN = "Bearer " + getToken("test15@mail.ru", "test15");
        mockMvc.perform(get("/api/user/profile/bookmarks")
                        .header(AUTHORIZATION, USER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[*].questionId").value(containsInRelativeOrder(101,102,103)))
                .andExpect(jsonPath("$.[*].listTagDto.length()").value(containsInRelativeOrder(2,2,2)))
                .andExpect(jsonPath("$.[*].countAnswer").value(containsInRelativeOrder(3,2,1)))
                .andExpect(jsonPath("$.[*].countVote").value(containsInRelativeOrder(2,1,-1)))
                .andExpect(jsonPath("$.[*].countView").value(containsInRelativeOrder(4,1,0)));
    }


    @Test
    @DataSet(cleanBefore = true, cleanAfter = true,
            value = {
                    "dataset/userresourcecontroller/users.yml",
                    "dataset/userresourcecontroller/roles.yml",
                    "dataset/testUserResourceController/getAllUserProfileQuestionDtoById/questions.yml",
                    "dataset/testUserResourceController/getAllUserProfileQuestionDtoById/tags.yml",
                    "dataset/testUserResourceController/getAllUserProfileQuestionDtoById/questions_has_tag.yml",
                    "dataset/testUserResourceController/getAllUserProfileQuestionDtoById/answers.yml"
            },
            strategy = SeedStrategy.CLEAN_INSERT
    )
    public void getAllUserProfileQuestionDtoById() throws Exception {

        String USER_TOKEN_TEST1 = "Bearer " + getToken("test15@mail.ru", "test15");
        mockMvc.perform(get("/api/user/profile/questions")
                        .header(AUTHORIZATION, USER_TOKEN_TEST1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[*].questionId").value(containsInRelativeOrder(101, 102, 103)))
                .andExpect(jsonPath("$.[*].listTagDto.length()").value(containsInRelativeOrder(2, 2, 0)))
                .andExpect(jsonPath("$.[*].countAnswer").value(containsInRelativeOrder(3, 0, 2)));

        String USER_TOKEN_TEST2 = "Bearer " + getToken("test102@mail.ru", "test15");
        mockMvc.perform(get("/api/user/profile/questions")
                        .header(AUTHORIZATION, USER_TOKEN_TEST2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}


















