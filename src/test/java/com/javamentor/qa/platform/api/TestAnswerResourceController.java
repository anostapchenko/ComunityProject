package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class TestAnswerResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Autowired
    private MockMvc mvc;

    private static final String PASSWORD = "password";

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/users.yml",
            "dataset/testAnswerResourceController/questions.yml",
            "dataset/testAnswerResourceController/answers.yml",
            "dataset/testAnswerResourceController/rep.yml",
            "dataset/testAnswerResourceController/voteAnswer.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "question",
                    "comment_answer",
                    "answer",
                    "votes_on_answers",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    @ExpectedDataSet(value = {
            "dataset/testAnswerResourceController/expected/reputation10.yml",
            "dataset/testAnswerResourceController/expected/voteAnswerUp.yml",
    },
    ignoreCols = {"id","persist_date"})
    public void shouldBeVoteUp() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/users.yml",
            "dataset/testAnswerResourceController/questions.yml",
            "dataset/testAnswerResourceController/answers.yml",
            "dataset/testAnswerResourceController/rep.yml",
            "dataset/testAnswerResourceController/voteAnswer.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "question",
                    "comment_answer",
                    "answer",
                    "votes_on_answers",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    public void shouldBeNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/999/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/users.yml",
            "dataset/testAnswerResourceController/questions.yml",
            "dataset/testAnswerResourceController/answers.yml",
            "dataset/testAnswerResourceController/rep.yml",
            "dataset/testAnswerResourceController/voteAnswer.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "question",
                    "comment_answer",
                    "answer",
                    "votes_on_answers",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    @ExpectedDataSet(value = {
            "dataset/testAnswerResourceController/expected/reputation1010.yml",
            "dataset/testAnswerResourceController/expected/voteAnswerUpUp.yml",
    },
            ignoreCols = {"id","persist_date"})
    public void shouldBeVoteUp2DifferentUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user102@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/users.yml",
            "dataset/testAnswerResourceController/questions.yml",
            "dataset/testAnswerResourceController/answers.yml",
            "dataset/testAnswerResourceController/rep.yml",
            "dataset/testAnswerResourceController/voteAnswer.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "question",
                    "comment_answer",
                    "answer",
                    "votes_on_answers",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    @ExpectedDataSet(value = {
            "dataset/testAnswerResourceController/expected/reputation10.yml",
            "dataset/testAnswerResourceController/expected/voteAnswerUp.yml",
    },
            ignoreCols = {"id","persist_date"})
    public void shouldBeVoteUp2SameUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/users.yml",
            "dataset/testAnswerResourceController/questions.yml",
            "dataset/testAnswerResourceController/answers.yml",
            "dataset/testAnswerResourceController/rep.yml",
            "dataset/testAnswerResourceController/voteAnswer.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "question",
                    "comment_answer",
                    "answer",
                    "votes_on_answers",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    @ExpectedDataSet(
            value = {
                    "dataset/testAnswerResourceController/expected/reputation10.yml",
                    "dataset/testAnswerResourceController/expected/voteAnswerUp.yml",
    },
            ignoreCols = {"id","persist_date"})
    public void shouldBeVoteUpAndVoteDownSameUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/downVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/users.yml",
            "dataset/testAnswerResourceController/questions.yml",
            "dataset/testAnswerResourceController/answers.yml",
            "dataset/testAnswerResourceController/rep.yml",
            "dataset/testAnswerResourceController/voteAnswer.yml"},
            tableOrdering = {
                    "role",
                    "user_entity",
                    "question",
                    "comment_answer",
                    "answer",
                    "votes_on_answers",
                    "reputation"
            },
            cleanBefore = true,
            strategy = SeedStrategy.INSERT)
    @ExpectedDataSet(value = {
            "dataset/testAnswerResourceController/expected/reputation10-5.yml",
            "dataset/testAnswerResourceController/expected/voteAnswerUpDown.yml",
    },
            ignoreCols = {"id","persist_date"})
    public void shouldBeVoteUpAndVoteDownDifferentUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/question/100/answer/100/downVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user102@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    @DataSet(
            value = {
                    "dataset/testAnswerResourceController/addAnswer/answers.yml"
            },
            cleanBefore = true, cleanAfter = true,
            strategy = SeedStrategy.CLEAN_INSERT)
    @ExpectedDataSet(value = {
            "dataset/testAnswerResourceController/addAnswer/expected/answers.yml"
    },
            ignoreCols = {"date_accept_time", "persist_date", "update_date"}
    )
    public void shouldAddAnswerAndReturnAnswerDto() throws Exception {

        String token100 = "Bearer " + getToken("user100@mail.ru", "password");
        String token101 = "Bearer " + getToken("user101@mail.ru", "user101");

        //добавляю новый ответ user 100 по вопросу 101
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/question/101/answer/add")
                        .contentType("application/json")
                        .content("answer # 1 about Question 1")
                        .header("Authorization", token100))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.htmlBody").value("answer # 1 about Question 1"));

        //добавляю новый ответ user 101 по вопросу 101
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/question/101/answer/add")
                        .contentType("application/json")
                        .content("answer # 2 about Question 1")
                        .header("Authorization", token101))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.htmlBody").value("answer # 2 about Question 1"));

        //добавляю новый ответ user 101 по несуществующему вопросу 999
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/question/999/answer/add")
                        .contentType("application/json")
                        .content("answer # 1 about Question 999")
                        .header("Authorization", token101))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/moreAnswers.yml",
            "dataset/testAnswerResourceController/moreQuestions.yml",
            "dataset/testAnswerResourceController/moreVoteAnswer.yml",
            "dataset/testAnswerResourceController/moreUsers.yml",
            "dataset/testAnswerResourceController/roles.yml",
            "dataset/testAnswerResourceController/expected/reputation10-5.yml"
    }
    )

    public void getAllAnswerDtoByQuestionId() throws Exception {

        String token = "Bearer " + getToken("user100@mail.ru", "password");

        //Тестируем корректный случай
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/100/answer")
                        .header("Authorization", token)
                        .contentType("application/json")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(100))
                .andExpect(jsonPath("$.[0].userId").value(100))
                .andExpect(jsonPath("$.[0].userReputation").value(5))
                .andExpect(jsonPath("$.[0].questionId").value(100))
                .andExpect(jsonPath("$.[0].htmlBody").value("Body of helpful and well written answer1"))
                .andExpect(jsonPath("$.[0].isHelpful").value("false"))
                .andExpect(jsonPath("$.[0].countValuable").value(2))
                .andExpect(jsonPath("$.[0].nickName").value("user0"))
                .andExpect(jsonPath("$.[1].id").value(101))
                .andExpect(jsonPath("$.[2].id").value(102))
                .andExpect(jsonPath("$.[3].id").value(103))
                .andExpect(jsonPath("$.size()").value(4));

        //Тестируем случай с вопросом без ответов
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/104/answer")
                        .header("Authorization", token)
                        .contentType("application/json")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/moreAnswers.yml",
            "dataset/testAnswerResourceController/moreQuestions.yml",
            "dataset/testAnswerResourceController/moreUsers.yml",
            "dataset/testAnswerResourceController/roles.yml"
    }, strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, cleanAfter = true)

    public void shouldDeleteAnswer() throws Exception {

        String token = "Bearer " + getToken("user100@mail.ru", "password");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/question/100/answer/100/delete")
                        .header("Authorization", token)
                        .contentType("application/json")
                ).andDo(print())
                .andExpect(status().isOk());
        assertThat((boolean) entityManager.createQuery(
                        "SELECT CASE WHEN a.isDeleted = TRUE THEN TRUE ELSE FALSE END " +
                                "FROM Answer a WHERE a.id =: id")
                .setParameter("id", (long) 100)
                .getSingleResult())
                .isEqualTo(true);
    }

    @Test
    @DataSet(value = {
            "dataset/testAnswerResourceController/moreAnswers.yml",
            "dataset/testAnswerResourceController/moreQuestions.yml",
            "dataset/testAnswerResourceController/moreUsers.yml",
            "dataset/testAnswerResourceController/roles.yml"
    }, strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, cleanAfter = true)

    public void shouldUpdateAnswer() throws Exception {

        String token = "Bearer " + getToken("user100@mail.ru", "password");

        // Изменяем ответ
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/100/answer/100/update")
                        .header("Authorization", token)
                        .content("modified answer about Question 100")
                        .contentType("application/json")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.htmlBody").value("modified answer about Question 100"));
        assertThat((boolean) entityManager.createQuery(
                        "SELECT CASE WHEN a.htmlBody =: htmlBody THEN TRUE ELSE FALSE END " +
                                "FROM Answer a WHERE a.id =: id")
                .setParameter("id", (long) 100)
                .setParameter("htmlBody", "modified answer about Question 100")
                .getSingleResult())
                .isEqualTo(true);

        // Изменяем несуществующий ответ
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/100/answer/999/update")
                        .header("Authorization", token)
                        .content("modified answer 999")
                        .contentType("application/json")
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

}
