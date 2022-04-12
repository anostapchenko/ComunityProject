package com.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$[0].description").exists())
        ;
    }

    public String getTokens(String email) throws Exception {
        String tokenJS = mockMvc.perform(post("/api/auth/token")
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
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[1].id").value(103))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].description").exists());
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
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[1].id").value(103))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].description").exists());
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
        //Проверка фильтрации тэгов по параметру filter=spr(4 записи должен найти)
        this.mockMvc.perform(get("/api/user/tag/new?page=1&filter=spr")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("4"))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(101, 106, 103,102)));
        //Проверка фильтрации тэгов по параметру filter=sprg(ничего не должен найти)
        this.mockMvc.perform(get("/api/user/tag/new?page=1&filter=sprg")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("0"))
                .andExpect(jsonPath("$.items.length()").value(0));
        //Проверка фильтрации тэгов при отсутсвии параметра filter(должне найти все тэги)
        this.mockMvc.perform(get("/api/user/tag/new?page=1")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("11"))
                .andExpect(jsonPath("$.items.length()").value(10));
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
        String token="Bearer "+ getToken("user102@mail.ru","test15");
        // указаны параметры page и items
        this.mockMvc.perform(get("/api/user/tag/name?page=1&items=5")
                .contentType("application/json")
                .header("Authorization", token))
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
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
        //Проверка фильтрации тэгов по параметру filter=t(2 записи должен найти)
        this.mockMvc.perform(get("/api/user/tag/name?page=1&filter=t")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("2"))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(107, 104)));
        //Проверка фильтрации тэгов по параметру filter=sprg(ничего не должен найти)
        this.mockMvc.perform(get("/api/user/tag/name?page=1&filter=sprg")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("0"))
                .andExpect(jsonPath("$.items.length()").value(0));
        //Проверка фильтрации тэгов при отсутсвии параметра filter(должне найти все тэги)
        this.mockMvc.perform(get("/api/user/tag/name?page=1")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("6"))
                .andExpect(jsonPath("$.items.length()").value(6));
    }

    // Все Tag по аттрибуту популярности(QuestionCount чем выше, тем более популярный тэг)
    @Test
    @DataSet(value = "dataset/testTagResourceController/TagsByPopular.yml",
            strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, cleanAfter = true)
    public void shouldReturnAllTagsSortByPopularQuest() throws Exception {
        String token="Bearer "+getToken("user102@mail.ru","test15");
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
        //Проверка фильтрации тэгов по параметру filter=t(2 записи должен найти)
        this.mockMvc.perform(get("/api/user/tag/popular?page=1&filter=g2")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("1"))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(102)));
        //Проверка фильтрации тэгов по параметру filter=t5(ничего не должен найти)
        this.mockMvc.perform(get("/api/user/tag/popular?page=1&filter=t5")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("0"))
                .andExpect(jsonPath("$.items.length()").value(0));
        //Проверка фильтрации тэгов при отсутсвии параметра filter(должне найти все тэги)
        this.mockMvc.perform(get("/api/user/tag/popular?page=1")
                        .contentType("application/json")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResultCount").value("4"))
                .andExpect(jsonPath("$.items.length()").value(4));
    }

    //Удаление IgnoredTag и TrackedTag
    @Test
    @DataSet(value= {
            "dataset/testTagResourceController/tag_ignore.yml",
            "dataset/testTagResourceController/trackedTag2.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/tag2.yml"
    },strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, cleanAfter = true)
    public void shouldDeleteIgnoredTagAndTrackedTag() throws Exception{
        mockMvc.perform(delete("/api/user/tag/ignored/delete?tag=102")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat((long) entityManager.createQuery(
                "SELECT COUNT(e) FROM IgnoredTag" +
                        " e WHERE e.id =: id and e.user.id =: userId")
                    .setParameter("id",(long) 101)
                    .setParameter("userId",(long) 102)
                    .getSingleResult() > 0)
                .isEqualTo(false);

        mockMvc.perform(delete("/api/user/tag/ignored/delete?tag=102")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getToken("user102@mail.ru","test15")))
                        .andDo(print())
                        .andExpect(status().isBadRequest());

        mockMvc.perform(delete("/api/user/tag/tracked/delete?tag=102")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat((long) entityManager.createQuery(
                "SELECT COUNT(e) FROM TrackedTag" +
                        " e WHERE e.id =: id and e.user.id =: userId")
                    .setParameter("id",(long) 100)
                    .setParameter("userId",(long) 100)
                    .getSingleResult() > 0)
                .isEqualTo(false);

        mockMvc.perform(delete("/api/user/tag/tracked/delete?tag=102")
                .contentType("application/json")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // Добавление IgnoredTag и TrackedTag
    @Test
    @DataSet(value = {
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/tag2.yml"
    }, strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, cleanAfter = true)
    public void shouldAddIgnoredTagAndTrackedTag() throws Exception {

        // Добавляем IgnoredTag два раза, второй раз добавить не должен.
        mockMvc.perform(post("/api/user/tag/ignored/add?tag=102")
                        .header("Authorization", "Bearer " +
                                getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.id").value("102"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.description").value("test1"));
        assertThat((long) entityManager.createQuery("SELECT COUNT(e) FROM IgnoredTag e")
                .getSingleResult() == 1).isEqualTo(true);
        mockMvc.perform(post("/api/user/tag/ignored/add?tag=102")
                        .header("Authorization", "Bearer " +
                                getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertThat((long) entityManager.createQuery("SELECT COUNT(e) FROM IgnoredTag e")
                .getSingleResult() == 1).isEqualTo(true);

        // Добавляем TrackedTag два раза, второй раз добавить не должен.
        mockMvc.perform(post("/api/user/tag/tracked/add?tag=103")
                        .header("Authorization", "Bearer " +
                                getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.id").value("103"))
                .andExpect(jsonPath("$.name").value("name2"))
                .andExpect(jsonPath("$.description").value("test2"));
        assertThat((long) entityManager.createQuery("SELECT COUNT(e) FROM TrackedTag e")
                .getSingleResult() == 1).isEqualTo(true);
        mockMvc.perform(post("/api/user/tag/tracked/add?tag=103")
                        .header("Authorization", "Bearer " +
                                getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertThat((long) entityManager.createQuery("SELECT COUNT(e) FROM TrackedTag e")
                .getSingleResult() == 1).isEqualTo(true);

        // Пробуем добавить IgnoredTag, когда он уже есть в TrackedTag и наоборот. Добавить не должен.
        mockMvc.perform(post("/api/user/tag/ignored/add?tag=103")
                .header("Authorization", "Bearer " +
                        getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertThat((long) entityManager.createQuery("SELECT COUNT(e) FROM IgnoredTag e")
                .getSingleResult() == 1).isEqualTo(true);

        mockMvc.perform(post("/api/user/tag/tracked/add?tag=102")
                .header("Authorization", "Bearer " +
                        getToken("user102@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertThat((long) entityManager.createQuery("SELECT COUNT(e) FROM TrackedTag e")
                .getSingleResult() == 1).isEqualTo(true);
    }
}
