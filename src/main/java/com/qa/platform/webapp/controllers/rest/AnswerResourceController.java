package com.qa.platform.webapp.controllers.rest;

import com.qa.platform.models.dto.AnswerDTO;
import com.qa.platform.models.entity.question.Question;
import com.qa.platform.models.entity.question.answer.Answer;
import com.qa.platform.models.entity.question.answer.VoteAnswer;
import com.qa.platform.models.entity.question.answer.VoteType;
import com.qa.platform.models.entity.user.User;
import com.qa.platform.service.abstracts.model.AnswerService;
import com.qa.platform.service.abstracts.model.QuestionService;
import com.qa.platform.service.abstracts.model.VoteAnswerService;
import com.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.qa.platform.webapp.converters.AnswerConverter;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "AnswerResourceController", description = "Позволяет работать с ответами на вопросы")
@RestController
@RequestMapping("api/user/question/{questionId}/answer")
public class AnswerResourceController {

    private final VoteAnswerService voteAnswerService;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final AnswerDtoService answerDtoService;
    private final AnswerConverter answerConverter;

    @Autowired
    public AnswerResourceController(VoteAnswerService voteAnswerService,
                                    AnswerService answerService,
                                    QuestionService questionService,
                                    AnswerDtoService answerDtoService, AnswerConverter answerConverter) {
        this.voteAnswerService = voteAnswerService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.answerDtoService = answerDtoService;
        this.answerConverter = answerConverter;
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
    @PostMapping(path = "/{id}/upVote")
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
    @PostMapping(path = "/{id}/downVote")
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
    @PostMapping("/add")
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
    @GetMapping
    public ResponseEntity<List<AnswerDTO>> getAnswers(@PathVariable Long questionId) {
        return new ResponseEntity<>(answerDtoService.getAllAnswerDtoByQuestionId(questionId), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление ответа",
            description = "Удаление ответа"
    )
    @ApiResponse(responseCode = "200", description = "Answer удален", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "Answer с таким id не существует",
            content = {
                    @Content(mediaType = "application/json")
            })
    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<?> deleteAnswer(@PathVariable(name = "id") long answerId) {
        if (answerService.existsById(answerId)) {
            answerService.deleteById(answerId);
            return new ResponseEntity<>("Answer was successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Answer with this id doesn't exist",
                HttpStatus.BAD_REQUEST);
    }

    @Operation(
            summary = "Редактирование ответа",
            description = "Редактирование ответа"
    )
    @ApiResponse(responseCode = "200", description = "Возвращает Answer, который был изменен", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnswerDTO.class))
    })
    @ApiResponse(responseCode = "400", description = "Answer с таким id не существует",
            content = {
            @Content(mediaType = "application/json")
    })
    @PutMapping(path = "/{id}/update")
        public ResponseEntity<?> updateAnswer(@PathVariable(name = "id") long answerId,
                                          @Valid @RequestBody String htmlBody) {
        Optional<AnswerDTO> answerDtoOpt = answerDtoService.getAnswerDtoById(answerId);
        if (answerDtoOpt.isEmpty()) {
            return new ResponseEntity<>("Can't find answer with id:" + answerId, HttpStatus.BAD_REQUEST);
        }
        AnswerDTO answerDTO = answerDtoOpt.get();
        answerDTO.setHtmlBody(htmlBody);
        answerService.update(answerConverter.answerDTOToAnswer(answerDTO));
        return new ResponseEntity<>(answerDTO, HttpStatus.OK);
    }
}

