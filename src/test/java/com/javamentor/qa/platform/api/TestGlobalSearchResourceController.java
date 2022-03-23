package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class TestGlobalSearchResourceController extends AbstractClassForDRRiderMockMVCTests {

    @Test
    @DataSet(
            value = {
                    "dataset/GlobalSearchResourceController/question.yml",
                    "dataset/testQuestionResourceController/tag.yml",
                    "dataset/GlobalSearchResourceController/questions_has_tag.yml",
                    "dataset/GlobalSearchResourceController/users.yml",
                    "dataset/testQuestionResourceController/role.yml",
                    "dataset/GlobalSearchResourceController/votes_on_questions.yml",
                    "dataset/GlobalSearchResourceController/question_viewed.yml",
                    "dataset/QuestionResourceController/answers_for_all_questions.yml"
            }
    )
    public void getQuestionGlobalSearch() throws Exception {
        //поиск по телу вопроса body:
        mockMvc.perform(get("/api/search?q=body:test7 body")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(7))
                .andExpect(jsonPath("$.items[0].description").value("test7 body"))
                .andExpect(jsonPath("$.items.length()").value(1));

        //поиск по заголовку вопроса title:
        mockMvc.perform(get("/api/search?q=title:test7 title")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(7))
                .andExpect(jsonPath("$.items[0].title").value("test7 title"))
                .andExpect(jsonPath("$.items.length()").value(1));

        //поиск по username вопроса user:
        mockMvc.perform(get("/api/search?q=user:test15")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(4));

        //поиск по нескольким тэгам и слову
        mockMvc.perform(get("/api/search?q=[testNameTag3][testNameTag4]test7 body")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(7))
                .andExpect(jsonPath("$.items[0].description").value("test7 body"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items.length()").value(1));

        //поиск по одному тэгу и слову
        mockMvc.perform(get("/api/search?q=[testNameTag3]body")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(2))
                .andExpect(jsonPath("$.items[0].description").value("test2 body"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(4))
                .andExpect(jsonPath("$.items[1].id").value(4))
                .andExpect(jsonPath("$.items[1].description").value("test4 body"))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items[2].id").value(7))
                .andExpect(jsonPath("$.items[2].description").value("test7 body"))
                .andExpect(jsonPath("$.items[2].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[2].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items.length()").value(3));

        //поиск по "точной фразе"
        mockMvc.perform(get("/api/search?q=\"test7 body\"")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(7))
                .andExpect(jsonPath("$.items[0].description").value("test7 body"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items.length()").value(1));

        //поиск сообщений с оценкой равно
        mockMvc.perform(get("/api/search?q=score:-1")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[1].id").value(2))
                .andExpect(jsonPath("$.items[2].id").value(3))
                .andExpect(jsonPath("$.items.length()").value(3));

        //поиск сообщений с оценкой больше или равно
        mockMvc.perform(get("/api/search?q=score:1..")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(4))
                .andExpect(jsonPath("$.items[1].id").value(5))
                .andExpect(jsonPath("$.items.length()").value(2));

        //поиск сообщений по просмотрам
        mockMvc.perform(get("/api/search?q=views:1..2")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[1].id").value(2))
                .andExpect(jsonPath("$.items.length()").value(2));

        //поиск сообщений по просмотрам
        mockMvc.perform(get("/api/search?q=views:1-2")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[1].id").value(2))
                .andExpect(jsonPath("$.items.length()").value(2));

        //поиск сообщений по ответам
        mockMvc.perform(get("/api/search?q=answers:0")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(5))
                .andExpect(jsonPath("$.items[1].id").value(6))
                .andExpect(jsonPath("$.items[2].id").value(7))
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    @DataSet(
            value = {
                    "dataset/testQuestionResourceController/question_different_date.yml",
                    "dataset/testQuestionResourceController/tag.yml",
                    "dataset/GlobalSearchResourceController/questions_has_tag.yml",
                    "dataset/QuestionResourceController/users.yml",
                    "dataset/testQuestionResourceController/role.yml",
                    "dataset/QuestionResourceController/votes_on_questions.yml"
            }
    )
    public void getQuestionGlobalSearchByTagsName() throws Exception {

        //получение пагинированного списка по одному тэгу
        mockMvc.perform(get("/api/search/tagged/testNameTag2")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(2))
                .andExpect(jsonPath("$.items.length()").value(1));

        //получение пагинированного списка по нескольким тэгам
        mockMvc.perform(get("/api/search/tagged/testNameTag3+testNameTag4")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[2].id").value(4))
                .andExpect(jsonPath("$.items[1].id").value(4))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items[2].id").value(7))
                .andExpect(jsonPath("$.items[2].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[2].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items.length()").value(3));

        mockMvc.perform(get("/api/search/tagged/testNameTag3+testNameTag4+-testNameTag2")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(4))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items[1].id").value(7))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id").value(3))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id").value(4))
                .andExpect(jsonPath("$.items.length()").value(2));

    }
}
