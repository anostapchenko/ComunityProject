package com.javamentor.qa.platform.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class TestQuestionResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @DataSet(value = {
            "dataset/testQuestionIdCommentResource/Fix/comment.yml",
            "dataset/testQuestionIdCommentResource/users.yml",
            "dataset/testQuestionIdCommentResource/commentquestion.yml",
            "dataset/testQuestionIdCommentResource/questions.yml",
            "dataset/testQuestionIdCommentResource/reputations.yml",
            "dataset/testQuestionIdCommentResource/roles.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true, cleanBefore = true
    )
    // Получение списка дто комментариев к вопросам
    public void shouldGetQuestionIdComment() throws Exception {
        mockMvc.perform(get("/api/user/question/1/comment")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getToken("test1@mail.ru","test15")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].questionId").value(1))
                .andExpect(jsonPath("$[0].lastRedactionDate").value("2021-12-13T23:09:52.716"))
                .andExpect(jsonPath("$[0].persistDate").value("2021-12-13T23:09:52.716"))
                .andExpect(jsonPath("$[0].text").value("Hello Test"))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].imageLink").value("photo"))
                .andExpect(jsonPath("$[0].reputation").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].questionId").value(1))
                .andExpect(jsonPath("$[1].lastRedactionDate").value("2021-12-13T23:09:52.716"))
                .andExpect(jsonPath("$[1].persistDate").value("2021-12-13T23:09:52.716"))
                .andExpect(jsonPath("$[1].text").value("Hello Test2"))
                .andExpect(jsonPath("$[1].userId").value(2))
                .andExpect(jsonPath("$[1].imageLink").value("photo"))
                .andExpect(jsonPath("$[1].reputation").value(500));
    }

    @Test
    //Голосуем ПРОТИВ вопроса (DOWN_VOTE) и получаем ответ с количеством голосов: 1 и репутацией -5
    @DataSet(cleanAfter = true, cleanBefore = true,
            value = "dataset/questionresourcecontroller/data.yml",
            strategy = SeedStrategy.REFRESH )
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
    @DataSet(cleanAfter = true, cleanBefore = true,
            value = "dataset/questionresourcecontroller/data.yml",
            strategy = SeedStrategy.REFRESH )
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
    @DataSet(cleanAfter = true, cleanBefore = true,
            value = "dataset/questionresourcecontroller/data2.yml",
            strategy = SeedStrategy.REFRESH )
    public void shouldValidateUserVoteDownVote() throws Exception {
        this.mockMvc.perform(post("/api/user/question/2/downVote").header("Authorization", "Bearer " + getToken("test15@mail.ru","test15"))).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User was voting")));
    }
    @Test
    //Повторно голосуем ЗА вопроса (UP_VOTE) и получаем ответ: "User was voting"
    // повторный голос не учитывается.
    @DataSet(cleanAfter = true, cleanBefore = true,
            value = "dataset/questionresourcecontroller/data2.yml",
            strategy = SeedStrategy.REFRESH )
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
            "dataset/QuestionResourceController/Fix/reputations.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true, cleanBefore = true
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
            "dataset/QuestionResourceController/Fix/reputations.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true, cleanBefore = true
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
            strategy = SeedStrategy.REFRESH,
            cleanAfter = true, cleanBefore = true
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
            strategy = SeedStrategy.REFRESH,
            cleanAfter = true, cleanBefore = true
    )
    //Обновляем один вопрос на удаленный и выводим только существующие
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void getNotOneQuestionCount() throws Exception {
        entityManager.createQuery("UPDATE Question q set q.isDeleted=true where q.id=1L").executeUpdate();
        mockMvc.perform(get("/api/user/question/count")
                        .header("Authorization", "Bearer " + getToken("test15@mail.ru", "test15")))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"
    },
            disableConstraints = true,
            cleanAfter = true, cleanBefore = true
    )
    void questionCreateDtoWithoutTitle() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("Description");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithoutDescription() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithoutTags() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithEmptyTitle() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("Description");
        questionCreateDto.setTitle("");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithEmptyDescription() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithEmptyTags() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        questionCreateDto.setTags(new ArrayList<>());

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithNameTagWhenExist() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        TagDto tagDto = new TagDto();
        tagDto.setName("TAG100");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        String sqlCount = "select CAST(count(tag.id) as int) from Tag tag where tag.name = 'TAG100'";
        int rowCount = (int) entityManager.createQuery(sqlCount).getSingleResult();
        Assertions.assertTrue(rowCount == 1);


        String sql = "select tag.id from Tag tag where tag.name = 'TAG100'";
        Long tagId = (long) entityManager.createQuery(sql).getSingleResult();
        Assertions.assertTrue(tagId == 100L);
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionCreateDtoWithNameTagWhenNotExist() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        String sqlCount = "select CAST(count(tag.id) as int) from Tag tag where tag.name = 'Test'";
        int rowCount = (int) entityManager.createQuery(sqlCount).getSingleResult();
        Assertions.assertTrue(rowCount == 1);

    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionHasBeenCreated() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("questionHasBeenCreated");
        questionCreateDto.setDescription("questionHasBeenCreated");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("TAG100");
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("TAG101");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        listTagDto.add(tagDto2);
        listTagDto.add(tagDto3);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String questionDtoJsonString = mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(questionDtoJsonString, "$.id");


        String sql = "select CAST(count(question.id) as int) from Question question where question.id =: questionDtoId";
        int rowCount = (int) entityManager.createQuery(sql).setParameter("questionDtoId", id.longValue()).getSingleResult();
        Assertions.assertTrue(rowCount == 1);
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void questionHasBeenCreated_CheckTagList() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("questionHasBeenCreated_CheckTagList");
        questionCreateDto.setDescription("questionHasBeenCreated_CheckTagList");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("TAG100");
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("TAG101");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        listTagDto.add(tagDto2);
        listTagDto.add(tagDto3);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String questionDtoJsonString = mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(questionDtoJsonString, "$.id");
        List<HashMap> tagListQuestionDto = JsonPath.read(questionDtoJsonString, "$.listTagDto");
        List<Integer> listId = tagListQuestionDto.stream().map(list -> (int) list.get("id")).collect(Collectors.toList());

        String sql = "select CAST(question_has_tag.tag_id as int)" +
                " from question_has_tag where question_has_tag.question_id = ?";
        List<Integer> listTag = entityManager.createNativeQuery(sql).setParameter(1, id).getResultList();

        Assertions.assertArrayEquals(listTag.toArray(), listId.toArray());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true,
            cleanAfter = true, cleanBefore = true)
    void checkFieldsQuestionInReturnedQuestionDto() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("checkFieldsReturnedQuestionDto");
        questionCreateDto.setDescription("checkFieldsReturnedQuestionDto");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("TAG100");
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("TAG101");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        listTagDto.add(tagDto2);
        listTagDto.add(tagDto3);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String questionDtoJsonString = mockMvc.perform(
                        post("/api/user/question")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(questionDtoJsonString, "$.id");

        String sql = "select question from Question question join fetch question.user where question.id =: questionDtoId";
        Question questionFromBase = entityManager.createQuery(sql, Question.class).setParameter("questionDtoId", id.longValue()).getSingleResult();

        Assertions.assertEquals(questionFromBase.getId(), id.longValue());
        Assertions.assertEquals(questionFromBase.getTitle(),
                (JsonPath.read(questionDtoJsonString, "$.title")));
        Assertions.assertEquals(questionFromBase.getDescription(),
                (JsonPath.read(questionDtoJsonString, "$.description")));
        Assertions.assertEquals(questionFromBase.getUser().getNickname(),
                (JsonPath.read(questionDtoJsonString, "$.authorName")));
        Assertions.assertEquals(questionFromBase.getUser().getId().intValue(),
                (int) JsonPath.read(questionDtoJsonString, "$.authorId"));
        Assertions.assertEquals(questionFromBase.getUser().getImageLink(),
                (JsonPath.read(questionDtoJsonString, "$.authorImage")));
        Assertions.assertEquals(questionFromBase.getPersistDateTime().toString().substring(0, 23),
                (JsonPath.read(questionDtoJsonString, "$.persistDateTime").toString().substring(0, 23)));
        Assertions.assertEquals(questionFromBase.getLastUpdateDateTime().toString().substring(0, 23),
                (JsonPath.read(questionDtoJsonString, "$.lastUpdateDateTime").toString().substring(0, 23)));
        Assertions.assertEquals(0, (int) JsonPath.read(questionDtoJsonString, "$.viewCount"));
        Assertions.assertEquals(0, (int) JsonPath.read(questionDtoJsonString, "$.countAnswer"));
        Assertions.assertEquals(0, (int) JsonPath.read(questionDtoJsonString, "$.countValuable"));
    }
}
