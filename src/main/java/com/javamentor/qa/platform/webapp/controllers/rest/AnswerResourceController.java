package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "AnswerResourceController", description = "Позволяет работать с ответами на вопросы")
@RestController
public class AnswerResourceController {

    private VoteAnswerService voteAnswerService;
    private AnswerService answerService;
    private QuestionService questionService;
    private AnswerDtoService answerDtoService;

    @Autowired
    public AnswerResourceController(VoteAnswerService voteAnswerService,
                                    AnswerService answerService,
                                    QuestionService questionService,
                                    AnswerDtoService answerDtoService) {
        this.voteAnswerService = voteAnswerService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.answerDtoService = answerDtoService;
    }

    @Operation(summary = "Голосовать \"за\" (Up Vote)", description =
            "Позволяет проголосовать за ответ положительно, " +
                    "добавляет голос UP_VOTE в votes_on_answer, " +
                    "добавляет +10 в reputation автору ответа")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает сумму голосов, где upVote = 1 и downVote = -1",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Long.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Not Found, если нет id нужного ответа")
    })
    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/upVote")
    public ResponseEntity<?> upVote(@PathVariable(name = "id") long answerId,
                                       Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return upDownVoteEvent(VoteType.UP_VOTE, answerId, currentUser);
    }

    @Operation(summary = "Голосовать \"против\" (Down Vote)", description =
            "Позволяет проголосовать за ответ отрицательно, " +
                    "добавляет голос DOWN_VOTE в votes_on_answer, " +
                    "добавляет -5 в reputation автору ответа")
    @Schema()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает сумму голосов, где upVote = 1 и downVote = -1",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Long.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Not Found, если нет id нужного ответа")
    })
    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/downVote")
    public ResponseEntity<?> downVote(
            @PathVariable(name = "id") long answerId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return upDownVoteEvent(VoteType.DOWN_VOTE, answerId, currentUser);
    }

    @Operation(summary = "Проверка и добавление голоса за ответ, возврат суммы голосов", description =
            "Проверяет, существует ли Answer по id, " +
                    "если нет - BadRequest, " +
                    "если да - проверяет, есть ли голос текущего пользователя," +
                    "если нет - добавляет и возвращает Ok + votes count," +
                    "если да - возвращает Ok + votes count")
    private ResponseEntity<?> upDownVoteEvent(VoteType vote, long answerId, User currentUser) {
        Optional<Answer> optional = answerService.getAnswerWithAuthor(answerId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>("The answer not found", HttpStatus.BAD_REQUEST);
        }
        if (!voteAnswerService.existsVoteByAnswerAndUser(answerId, currentUser.getId())) {
            voteAnswerService.persist(new VoteAnswer(currentUser, optional.get(), vote));
        }
        return new ResponseEntity<>(voteAnswerService.getVoteCount(answerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Добавление ответа",
            description = "Добавление ответа"
    )
    @ApiResponse(responseCode = "200", description = "Ответ добавлен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AnswerDTO.class))
    })
    @ApiResponse(responseCode = "400", description = "Ответ не добавлен", content = {
            @Content(mediaType = "application/json")
    })
    @PostMapping("api/user/question/{questionId}/answer/add")
    public ResponseEntity<?> createAnswer(@PathVariable Long questionId,
                                          @Valid @RequestBody String bodyAnswer,
                                          Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Optional<Question> question = questionService.getById(questionId);

        if (question.isEmpty()){
            return new ResponseEntity<>("There is no question " + questionId.toString(), HttpStatus.BAD_REQUEST);
        }

        Answer answer = new Answer(question.get(), user, bodyAnswer);
        answerService.persist(answer);

        return new ResponseEntity<>(answerDtoService.getAnswerDtoById(answer.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка ответов на вопрос оп ID вопроса",
            description = "Получение списка всех ответов на вопрос по questionId, questionId - id вопроса")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список AnswerDto (id, userId, userReputation" +
                            "questionId, htmlBody, persistDateTime, isHelpful, dateAccept," +
                            " countValuable, image, nickName",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    @GetMapping("api/user/question/{questionId}/answer")
    public ResponseEntity<List<AnswerDTO>> getAnswers(@PathVariable Long questionId) {
        return new ResponseEntity<>(answerDtoService.getAllAnswerDtoByQuestionId(questionId), HttpStatus.OK);
    }
}

