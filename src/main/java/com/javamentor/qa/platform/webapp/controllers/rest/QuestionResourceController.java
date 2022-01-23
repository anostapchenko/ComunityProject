package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageWithoutAnswerDtoDao;
import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Question Resource Controller", description = "Управление сущностями, которые связаны с вопросами")
public class QuestionResourceController {

    private final
    QuestionService questionService;

    private final
    VoteQuestionService voteQuestionService;

    private final
    ReputationService reputationService;

    private final
    QuestionDtoService questionDtoService;

    public QuestionResourceController(QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService, QuestionDtoService questionDtoService) {
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
        this.questionDtoService = questionDtoService;
    }

    @GetMapping("api/user/question/count")
    @Operation(summary = "Количество всего вопросов в бд")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))
    })
    @ApiResponse(responseCode = "400", description = "Неверные учетные данные", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<Optional<Long>> getCountQuestion() {
        Optional<Long> countQusetion = questionService.getCountByQuestion();
        return new ResponseEntity<>(countQusetion, HttpStatus.OK);
    }

    @PostMapping("api/user/question/{questionId}/upVote")
    @Operation(
            summary = "Голосование ЗА вопрос",
            description = "Устанавливает голос +1 за вопрос и +10 к репутации автора вопроса"
    )
    public ResponseEntity<?> upVote(@PathVariable("questionId") Long questionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();
        Question question = questionService
                .getQuestionByIdWithAuthor(questionId)
                .orElseThrow(() -> new ConstrainException("Can't find question with id:" + questionId));
        int countUpVote = 10;
        if (voteQuestionService.validateUserVoteByQuestionIdAndUserId(questionId, userId)) {
            VoteQuestion voteQuestion = new VoteQuestion(user, question, VoteType.UP_VOTE, countUpVote);
            voteQuestionService.persist(voteQuestion);
            return new ResponseEntity<>(voteQuestionService.getVoteByQuestionId(questionId), HttpStatus.OK);
        }
        return new ResponseEntity<>("User was voting", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("api/user/question/{questionId}/downVote")
    @Operation(
            summary = "Голосование ПРОТИВ вопроса",
            description = "Устанавливает голос -1 за вопрос и -5 к репутации автора вопроса"
    )
    public ResponseEntity<?> downVote(@PathVariable("questionId") Long questionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();
        Question question = questionService
                .getQuestionByIdWithAuthor(questionId)
                .orElseThrow(() -> new ConstrainException("Can't find question with id:" + questionId));
        int countDownVote = -5;
        if (voteQuestionService.validateUserVoteByQuestionIdAndUserId(questionId, userId)) {
            VoteQuestion voteQuestion = new VoteQuestion(user, question, VoteType.DOWN_VOTE, countDownVote);
            voteQuestionService.persist(voteQuestion);
            return new ResponseEntity<>(voteQuestionService.getVoteByQuestionId(questionId), HttpStatus.OK);
        }
        return new ResponseEntity<>("User was voting", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/user/question/{id}")
    @Operation(summary = "Получение информации по вопросу пользователя")
    @ApiResponse(responseCode = "200", description = "Информация по вопросу", content = {
            @Content(mediaType = "application/json")
    })

    public ResponseEntity<?> getQuestion(@PathVariable Long id) {
        Optional<QuestionDto> q = questionDtoService.getQuestionDtoServiceById(id);
        if (q.isPresent()) {
            return new ResponseEntity<>(q.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Question number not exist!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/user/question/noAnswer")
    @Operation(summary = "Получение пагинированного списка всех вопросов, на которые еще не дан ответ. " +
            "В запросе указываем page - номер страницы, items (по умолчанию 10) - количество результатов на странице",
            description = "Получение пагинированного списка всех вопросов, на которые еще не дан ответ.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает пагинированный список PageDTO<QuestionDTO> (id, title, authorId" +
                            "authorReputation, authorName, authorImage, description, viewCount, countAnswer" +
                            "countValuable, LocalDateTime, LocalDateTime, listTagDto",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    public ResponseEntity<PageDTO<QuestionDto>> getQuestionsWithNoAnswer(@RequestParam int page,
                                                                         @RequestParam(required = false, defaultValue = "10") int items,
                                                                         @RequestParam(required = false) String trackedTags,
                                                                         @RequestParam(required = false) String ignoredTags) {

        PaginationData data = new PaginationData(page, items, QuestionPageWithoutAnswerDtoDao.class.getSimpleName());

        PageDTO<QuestionDto> pageDTO = questionDtoService.getPageDto(data);
        List<QuestionDto> questionDtoList = pageDTO.getItems();

        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

            if(trackedTags != null) {
                List<TagDto> trackedTagList = objectMapper.readValue(trackedTags, new TypeReference<>() {});
                questionDtoList.removeIf(dto -> !dto.getListTagDto().containsAll(trackedTagList));
            }
            if(ignoredTags != null) {
                List<TagDto> ignoredTagList = objectMapper.readValue(ignoredTags, new TypeReference<>() {});
                questionDtoList.removeIf(dto -> !Collections.disjoint(dto.getListTagDto(), ignoredTagList));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }
}

