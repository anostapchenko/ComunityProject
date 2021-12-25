package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AnswerResourceController", description = "Позволяет голосовать за ответ на вопрос")
@RestController
public class AnswerResourceController {

    private VoteAnswerService voteAnswerService;
    private AnswerService answerService;

    @Operation(summary = "Внедрение зависимости", description =
            "Внедрение зависимости VoteAnswerService и AnswerService")
    @Autowired
    public void setVoteAnswerServiceImpl(VoteAnswerService voteAnswerServiceImpl,
                                         AnswerService answerServiceImpl) {
        this.voteAnswerService = voteAnswerServiceImpl;
        this.answerService = answerServiceImpl;
    }

    @Operation(summary = "Голосовать \"за\" (Up Vote)", description =
            "Позволяет проголосовать за ответ положительно, " +
                    "добавляет голос UP_VOTE в votes_on_answer, " +
                    "добавляет +10 в reputation автору ответа")
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
    })
    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/downVote")
    public ResponseEntity<Long> downVote(
            @PathVariable(name = "id") long answerId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return upDownVoteEvent(VoteType.DOWN_VOTE, answerId, currentUser);
    }

    @Operation(summary = "Проверка, добавление, изменение голоса за ответ", description =
            "Проверяет, существует ли Answer по id, " +
                    "если нет - NotFound, " +
                    "если да - проверяет, есть ли голос текущего пользователя," +
                    "если нет - добавляет и возвращает Ok + votes count," +
                    "если да - проверят, совпадает ли принятый голос с существующим," +
                    "если нет - изменяет и возвращает Ok + votes count," +
                    "если да - возвращает Ok + votes count")
    private ResponseEntity<Long> upDownVoteEvent(VoteType vote, long answerId, User currentUser) {
        if (!answerService.existsById(answerId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!voteAnswerService.existsVoteAnswerByAnswerIdAndUserId(answerId, currentUser.getId())) {
            voteAnswerService.addVoteAnswer(vote, answerId, currentUser);
        } else {
            VoteAnswer voteAnswer = voteAnswerService.getVoteAnswerByAnswerIdAndUserId(answerId, currentUser.getId());
            if (!voteAnswer.getVote().equals(vote)) {
                voteAnswer.setVote(vote);
                voteAnswerService.updateVoteAnswer(voteAnswer, answerId, currentUser.getId());
            }
        }
        return new ResponseEntity<>(voteAnswerService.getVoteCount(answerId), HttpStatus.OK);
    }
}
