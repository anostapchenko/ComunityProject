package com.javamentor.qa.platform.api;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUserResourceController extends AbstractClassForDRRiderMockMVCTests {

    private static final String USERNAME = "user100@mail.ru";
    private static final String PASSWORD = "password";
    private static final String URL = "/api/user/vote?";

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
                get(URL + "page=1&items=2")
                .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[1].id").value(101));
        mockMvc.perform(
                get(URL + "page=2&items=2")
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
                get(URL + "page=1&items=2")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[1].id").value(101));
        mockMvc.perform(
                get(URL + "page=2&items=2")
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
                get(URL + "page=1")
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
                get(URL + "page=1&items=10")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(10))
                .andExpect(jsonPath("$.items[0].id").value(117))
                .andExpect(jsonPath("$.items[1].id").value(118))
                .andExpect(jsonPath("$.items[2].id").value(119));
        mockMvc.perform(
                get(URL + "page=2&items=10")
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
                get(URL + "page=1")
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
                get(URL + "page=")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                get(URL + "page=-1")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                get(URL + "page=0")
                        .header("Authorization", "Bearer " + getToken(USERNAME, PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
