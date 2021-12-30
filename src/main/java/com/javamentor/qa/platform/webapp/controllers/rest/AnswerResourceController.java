package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByBodyImpl;
import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByIdImpl;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "AnswerResourceController", description = "Позволяет работать с ответами на вопросы")
@RestController
@RequestMapping("/api/user/answer")
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

    @GetMapping("/id")
    public ResponseEntity<PageDTO<AnswerDTO>> paginationById(@RequestParam int page, @RequestParam int items) {
        PaginationData data = new PaginationData(page, items,
                AnswerPageDtoDaoByIdImpl.class.getSimpleName());
        return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);

    }

    @GetMapping("/html_body")
    public ResponseEntity<PageDTO<AnswerDTO>> paginationByHtmlBody(@RequestParam int page, @RequestParam int items) {
        PaginationData data = new PaginationData(page, items,
                AnswerPageDtoDaoByBodyImpl.class.getSimpleName());
        return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);
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
}

