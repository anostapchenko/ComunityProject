package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Question Resource Controller", description = "Управление сущностями, которые связаны с вопросами")
public class QuestionResourceController {

    private final QuestionService questionService;
    private final VoteQuestionService voteQuestionService;
    private final ReputationService reputationService;
    private final QuestionDtoService questionDtoService;
    private final QuestionConverter questionConverter;
    private final TagConverter tagConverter;

    public QuestionResourceController(QuestionService questionService,
                                      VoteQuestionService voteQuestionService,
                                      ReputationService reputationService,
                                      QuestionDtoService questionDtoService,
                                      QuestionConverter questionConverter,
                                      TagConverter tagConverter
                                      ) {
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
        this.questionDtoService = questionDtoService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
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
        User user =(User) auth.getPrincipal();
        Long userId = user.getId();
        Question question = questionService
                .getQuestionByIdWithAuthor(questionId)
                .orElseThrow(() -> new ConstrainException("Can't find question with id:" + questionId));
        int countUpVote = 10;
        if (voteQuestionService.validateUserVoteByQuestionIdAndUserId(questionId, userId)) {
            VoteQuestion voteQuestion = new VoteQuestion(user,question,VoteType.UP_VOTE,countUpVote);
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
        User user =(User) auth.getPrincipal();
        Long userId = user.getId();
        Question question = questionService
                .getQuestionByIdWithAuthor(questionId)
                .orElseThrow(() -> new ConstrainException("Can't find question with id:" + questionId));
        int countDownVote = -5;
        if (voteQuestionService.validateUserVoteByQuestionIdAndUserId(questionId, userId)) {
            VoteQuestion voteQuestion = new VoteQuestion(user,question,VoteType.DOWN_VOTE,countDownVote);
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




    @Operation(
            summary = "Добавление вопроса",
            description = "Добавление нового вопроса"
    )
    @ApiResponse(responseCode = "200", description = "Вопрос добавлен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionCreateDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Вопрос не добавлен", content = {
            @Content(mediaType = "application/json")
    })
    @PostMapping("api/user/question")
    public ResponseEntity<?> createNewQuestion(@Valid @RequestBody QuestionCreateDto questionCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Question question = questionConverter.questionDtoToQuestion(questionCreateDto);

        question.setUser((User) authentication.getPrincipal());
        question.setTags(tagConverter.listTagDtoToListTag(questionCreateDto.getTags()));

        questionService.persist(question);
        return new ResponseEntity<>(questionConverter.questionToQuestionDto(question), HttpStatus.OK);


//        Long userId = user.getId();
//        Question question = questionService.
//                .getQuestionByIdWithAuthor(questionId)
//                .orElseThrow(() -> new ConstrainException("Can't find question with id:" + questionId));
//        int countUpVote = 10;
//        if (voteQuestionService.validateUserVoteByQuestionIdAndUserId(questionId, userId)) {
//            VoteQuestion voteQuestion = new VoteQuestion(user,question,VoteType.UP_VOTE,countUpVote);
//            voteQuestionService.persist(voteQuestion);
//            return new ResponseEntity<>(voteQuestionService.getVoteByQuestionId(questionId), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Question not create", HttpStatus.BAD_REQUEST);
    }



}

