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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .post("http://localhost:8091/api/user/question/100/answer/100/upVote")
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
                .post("http://localhost:8091/api/user/question/100/answer/999/upVote")
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
                .post("http://localhost:8091/api/user/question/100/answer/100/upVote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + getToken("user101@mail.ru", PASSWORD))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        mvc.perform(MockMvcRequestBuilders
                .post("http://localhost:8091/api/user/question/100/answer/100/upVote")
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
}
