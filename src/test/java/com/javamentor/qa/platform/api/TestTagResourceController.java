package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestTagResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Test
    //У пользователя с id = 102 есть игнорируемые теги
    @DataSet(cleanBefore = true,
            value = {
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/tag_ignore.yml",
            "dataset/testTagResourceController/tag2.yml"
            },
            strategy = SeedStrategy.REFRESH )
    public void shouldReturnListIrnoredTag() throws Exception {
        this.mockMvc.perform(get("/api/user/tag/ignored")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class))).
                andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("102"))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].persistDateTime").exists())
        ;
    }

    public String getTokens(String email) throws Exception {
        String tokenJS = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\" : \"" + email + "\"," +
                        " \"password\" : \"password\"}")
        ).andReturn().getResponse().getContentAsString();
        return new JSONObject(tokenJS).getString("token");
    }

    @Test
    @DataSet(cleanBefore = true,
            value = {
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/emptyTrackedTag.yml",
            "dataset/testTagResourceController/emptyTag.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void ifEmptyTrackedTag() throws Exception {
        mockMvc.perform(get("/api/user/tag/tracked")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DataSet(cleanBefore = true,
            value = {
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/trackedTag2.yml",
            "dataset/testTagResourceController/tag2.yml",
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void ifHasTwoTags() throws Exception {
        mockMvc.perform(get("/api/user/tag/tracked")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].persistDateTime").exists())
                .andExpect(jsonPath("$[1].id").value(103))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].persistDateTime").exists());
    }

    @Test
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/testTagResourceController/roles.yml",
                    "dataset/testTagResourceController/users.yml",
                    "dataset/testTagResourceController/trackedTag3.yml",
                    "dataset/testTagResourceController/tag3.yml"
            },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void ifHasTwoTagsAndOneOther() throws Exception {
        mockMvc.perform(get("/api/user/tag/tracked")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].persistDateTime").exists())
                .andExpect(jsonPath("$[1].id").value(103))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].persistDateTime").exists());
    }

    @Test
    @DataSet(value = "dataset/testTagResourceController/popularTags.yml", strategy = SeedStrategy.CLEAN_INSERT)
    public void shouldReturnSortedByCountQuestionDesc() throws Exception {
        mockMvc.perform(get("/api/user/tag/related")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(103))
                .andExpect(jsonPath("$[0].countQuestion").value(3))
                .andExpect(jsonPath("$[1].countQuestion").value(2))
                .andExpect(jsonPath("$[2].countQuestion").value(2))
                .andExpect(jsonPath("$[3].id").value(101))
                .andExpect(jsonPath("$[3].countQuestion").value(1));
    }

    @Test
    @DataSet(cleanBefore = true, value = "dataset/testTagResourceController/popularTagsNoTags.yml", strategy = SeedStrategy.CLEAN_INSERT)
    public void shouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/user/tag/related")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DataSet(cleanBefore = true,
            value = "dataset/testTagResourceController/getTagsLike/tagsLike.yml",
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void shouldReturnTop10PopularTagsLikeLowCase() throws Exception {
        mockMvc.perform(get("/api/user/tag/latter?value=j")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.size()").value(3))

                .andExpect(jsonPath("$[0].id").value(104))
                .andExpect(jsonPath("$[0].name").value("JPA"))

                .andExpect(jsonPath("$[1].id").value(109))
                .andExpect(jsonPath("$[1].name").value("JUnit"))

                .andExpect(jsonPath("$[2].id").value(111))
                .andExpect(jsonPath("$[2].name").value("JAVA CORE"));
    }

    @Test
    @DataSet(cleanBefore = true,
            value = "dataset/testTagResourceController/getTagsLike/tagsLike.yml",
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void shouldReturnTop10PopularTagsLikeUpCase() throws Exception {
        mockMvc.perform(get("/api/user/tag/latter?value=J")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.size()").value(3))

                .andExpect(jsonPath("$[0].id").value(104))
                .andExpect(jsonPath("$[0].name").value("JPA"))

                .andExpect(jsonPath("$[1].id").value(109))
                .andExpect(jsonPath("$[1].name").value("JUnit"))

                .andExpect(jsonPath("$[2].id").value(111))
                .andExpect(jsonPath("$[2].name").value("JAVA CORE"));
    }

    @Test
    @DataSet(cleanBefore = true,
            value = "dataset/testTagResourceController/getTagsLike/tagsLike.yml",
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void shouldReturnTop10PopularTagsLikeEmptyValue() throws Exception {
        mockMvc.perform(get("/api/user/tag/latter?value=")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(10));
    }

    @Test
    @DataSet(cleanBefore = true,
            value = "dataset/testTagResourceController/getTagsLike/tagsLike.yml",
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void shouldReturnTop10PopularTagsLikeSQLInjection() throws Exception {
        mockMvc.perform(get("/api/user/tag/latter?value=j or true")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }


    @Test
    @DataSet(cleanBefore = true,
            value = "dataset/testTagResourceController/getTagsLike/tagsLike.yml",
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void shouldReturnTop10PopularTagsLike2() throws Exception {
        mockMvc.perform(get("/api/user/tag/latter?value=spring")
                        .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4))

                .andExpect(jsonPath("$[0].id").value(103))
                .andExpect(jsonPath("$[0].name").value("spring boot"));
    }

    @Test
    @DataSet(cleanBefore = true,
            value = "dataset/testTagResourceController/getTagsLike/tagsLike.yml",
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void shouldReturnPaginationItems() throws Exception {

        String token = "Bearer " + getTokens("user100@mail.ru");

        //не заполнен реквизит items
        this.mockMvc.perform(get("/api/user/tag/new?page=1")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"))
                .andExpect(jsonPath("$.totalResultCount").value("11"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(110, 101, 104, 111, 105, 108, 106, 103, 109, 107)));

        //не заполнен реквизит items стрица 2
        this.mockMvc.perform(get("/api/user/tag/new?page=2")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("2"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.itemsOnPage").value("1"))
                .andExpect(jsonPath("$.totalResultCount").value("11"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(102)));

        //заполнены все параметры
        this.mockMvc.perform(get("/api/user/tag/new?page=2&items=3")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("2"))
                .andExpect(jsonPath("$.totalPageCount").value("4"))
                .andExpect(jsonPath("$.itemsOnPage").value("3"))
                .andExpect(jsonPath("$.totalResultCount").value("11"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(111, 105, 108)));

        // нет обязательного параметра - page
        mockMvc.perform(get("/api/user/tag/new?items=3")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
//  Получаем все Tag из БД отсортированных по имени
    @DataSet(cleanBefore = true,
            value = {
                    "dataset/testTagResourceController/roles.yml",
                    "dataset/testTagResourceController/users.yml",
                    "dataset/testTagResourceController/tag5.yml"
            },
            strategy = SeedStrategy.REFRESH)
    public void shouldReturnAllTagSortByName() throws Exception {
        // указаны параметры page и items
        this.mockMvc.perform(get("/api/user/tag/name?page=1&items=5")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.itemsOnPage").value("5"))
                .andExpect(jsonPath("$.totalResultCount").value("6"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(102, 105, 106)));


        // нет обязательного параметра - page
        mockMvc.perform(get("/api/user/tag/name?items=3")
                .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // Все Tag по аттрибуту популярности(QuestionCount чем выше, тем более популярный тэг)
    @Test
    @DataSet(value = "dataset/testTagResourceController/TagsByPopular.yml",
            strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, cleanAfter = true)
    public void shouldReturnAllTagsSortByPopularQuest() throws Exception {
        mockMvc.perform(get("/api/user/tag/popular?page=1&items=4")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("1"))
                .andExpect(jsonPath("$.itemsOnPage").value("4"))
                .andExpect(jsonPath("$.totalResultCount").value("4"))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(103, 102, 104, 101)));
    }
}
