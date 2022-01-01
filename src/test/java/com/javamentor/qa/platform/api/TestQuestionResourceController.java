package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.junit.jupiter.api.Test;

import javax.persistence.Query;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestQuestionResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Test
    //Голосуем ПРОТИВ вопроса (DOWN_VOTE) и получаем ответ с количеством голосов: 1 и репутацией -5
    @DataSet(cleanBefore = true, value = "dataset/questionresourcecontroller/data.yml", strategy = SeedStrategy.REFRESH )
    public void shouldReturnSetupDownVoteDownReputation() throws Exception {
        this.mockMvc.perform(post("/api/user/question/2/downVote").header("Authorization", "Bearer " + getToken("test15@mail.ru","test15"))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
        Query queryValidateUserVote = entityManager.createQuery("select v from Reputation v join fetch v.question join fetch v.sender where (v.sender.id in :userId) and (v.question.id in : id )  ", Reputation.class);
        queryValidateUserVote.setParameter("userId",15L);
        queryValidateUserVote.setParameter("id",2L);
        Reputation reputation = (Reputation) queryValidateUserVote.getSingleResult();
        assertThat(reputation.getCount()).isEqualTo(-5);
    }

    @Test
    @DataSet(cleanBefore = true, value = "dataset/questionresourcecontroller/data.yml", strategy = SeedStrategy.REFRESH )
    //Голосуем ЗА вопрос (UP_VOTE) и получаем ответ с количеством голосов: 1 и репутация увеличена на +10.
    public void shouldReturnSetupUpVoteUpReputation() throws Exception {
        this.mockMvc.perform(post("/api/user/question/1/upVote").header("Authorization", "Bearer " + getToken("test15@mail.ru","test15"))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
        Query queryValidateUserVote = entityManager.createQuery("select v from Reputation v join fetch v.question join fetch v.sender where (v.sender.id in :userId) and (v.question.id in : id )  ", Reputation.class);
        queryValidateUserVote.setParameter("userId",15L);
        queryValidateUserVote.setParameter("id",1L);
        Reputation reputation = (Reputation) queryValidateUserVote.getSingleResult();
        assertThat(reputation.getCount()).isEqualTo(10);
    }
    @Test
    //Повторно голосуем ПРОТИВ вопроса (DOWN_VOTE) и получаем ответ: "User was voting"
    // повторный голос не учитывается.
    @DataSet(cleanBefore = true,value = "dataset/questionresourcecontroller/data2.yml", strategy = SeedStrategy.REFRESH )
    public void shouldValidateUserVoteDownVote() throws Exception {
        this.mockMvc.perform(post("/api/user/question/2/downVote").header("Authorization", "Bearer " + getToken("test15@mail.ru","test15"))).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User was voting")));
    }
    @Test
    //Повторно голосуем ЗА вопроса (UP_VOTE) и получаем ответ: "User was voting"
    // повторный голос не учитывается.
    @DataSet(cleanBefore = true, value = "dataset/questionresourcecontroller/data2.yml", strategy = SeedStrategy.REFRESH )
    public void shouldValidateUserVoteUpVote() throws Exception {
        this.mockMvc.perform(post("/api/user/question/1/upVote").header("Authorization", "Bearer " + getToken("test15@mail.ru","test15"))).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User was voting")));
    }
    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/roles.yml",
            "dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tags.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/questions_has_tag.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true
    )
    // Получение json по существующему вопросу
    public void getCorrectQuestionDtoByIdTest() throws Exception {
        mockMvc.perform(get("/api/user/question/1")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.authorId").value(15))
                .andExpect(jsonPath("$.authorReputation").value(100))
                .andExpect(jsonPath("$.authorName").value("test 15"))
                .andExpect(jsonPath("$.authorImage").value("photo"))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.viewCount").value(0L))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.countValuable").value(-1))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.listTagDto[0].description").value("testDescriptionTag"))
                .andExpect(jsonPath("$.listTagDto[0].name").value("testNameTag"))
                .andExpect(jsonPath("$.listTagDto[0].id").value(1));
    }
    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/roles.yml",
            "dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tags.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/questions_has_tag.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true
    )
    // получение ответа по не существующему в тестовой базе вопросу
    public void getWrongQuestionDtoByIdTest() throws Exception {
        mockMvc.perform(get("/api/user/question/2")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/roles.yml",
            "dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/questions.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true
    )
    // получение количество вопросов
    public void getQuestionCount() throws Exception {
        mockMvc.perform(get("/api/user/question/count")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru","test15")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {
            "dataset/QuestionResourceController/roles.yml",
            "dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/questions.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true
    )
    // Получение json по существующему вопросу
    public void getNotOneQuestionCount() throws Exception {
        mockMvc.perform(get("/api/user/question/count")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.is_deleted").value(false))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-13T18:09:52.716"))
                .andExpect(jsonPath("$.title").value("test"));
    }
}
