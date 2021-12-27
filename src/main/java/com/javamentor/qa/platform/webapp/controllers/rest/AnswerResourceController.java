package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByBodyImpl;
import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByIdImpl;
import com.javamentor.qa.platform.exception.NoSuchDaoException;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import com.javamentor.qa.platform.service.abstracts.pagination.AnswerPageDtoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AnswerResourceController", description = "Позволяет работать с ответами на вопросы")
@RestController
public class AnswerResourceController {

    private final AnswerPageDtoService answerDtoService;
    private VoteAnswerService voteAnswerService;
    private AnswerService answerService;

    @Autowired
    public AnswerResourceController(AnswerPageDtoService answerDtoService,
                                    VoteAnswerService voteAnswerService,
                                    AnswerService answerService) {
        this.answerDtoService = answerDtoService;
        this.voteAnswerService = voteAnswerService;
        this.answerService = answerService;
    }

    @GetMapping("/api/answer/id")
    public ResponseEntity<Object> paginationById(@RequestBody PaginationData data) {
        try {
            data.setDaoName(AnswerPageDtoDaoByIdImpl.class.getSimpleName());
            return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);
        } catch (NoSuchDaoException e) {
            return new ResponseEntity<>("Wrong dao name", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/answer/html_body")
    public ResponseEntity<Object> paginationByHtmlBody(@RequestBody PaginationData data) {
        try {
            data.setDaoName(AnswerPageDtoDaoByBodyImpl.class.getSimpleName());
            return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);
        } catch (NoSuchDaoException e) {
            return new ResponseEntity<>("Wrong dao name", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Long> upVote(@PathVariable(name = "id") long answerId,
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
    public ResponseEntity<Long> downVote(
            @PathVariable(name = "id") long answerId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return upDownVoteEvent(VoteType.DOWN_VOTE, answerId, currentUser);
    }

    @Operation(summary = "Проверка и добавление голоса за ответ, возврат суммы голосов", description =
            "Проверяет, существует ли Answer по id, " +
                    "если нет - NotFound, " +
                    "если да - проверяет, есть ли голос текущего пользователя," +
                    "если нет - добавляет и возвращает Ok + votes count," +
                    "если да - возвращает Ok + votes count")
    private ResponseEntity<Long> upDownVoteEvent(VoteType vote, long answerId, User currentUser) {
        if (!answerService.existsById(answerId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!voteAnswerService.existsVoteByAnswerAndUser(answerId, currentUser.getId())) {
            voteAnswerService.addVoteAnswer(vote, answerId, currentUser);
        }
        return new ResponseEntity<>(voteAnswerService.getVoteCount(answerId), HttpStatus.OK);
    }
}

