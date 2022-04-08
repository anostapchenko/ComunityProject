package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.*;
import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageDtoDaoAllSortedByPopular;
import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageDtoDaoAllQuestionsImpl;
import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageDtoDaoByNoAnswersImpl;
import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageDtoDaoByTagId;
import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageDtoDaoSortedByDate;
import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.question.CommentQuestion;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import com.javamentor.qa.platform.service.abstracts.model.BookmarksService;
import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    private final TagDtoService tagDtoService;
    private final QuestionViewedService questionViewedService;
    private final BookmarksService bookmarksService;

    public QuestionResourceController(QuestionService questionService,
                                      VoteQuestionService voteQuestionService,
                                      ReputationService reputationService,
                                      QuestionDtoService questionDtoService,
                                      QuestionConverter questionConverter,
                                      TagConverter tagConverter,
                                      TagDtoService tagDtoService,
                                      QuestionViewedService questionViewedService,
                                      BookmarksService bookmarksService) {
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
        this.questionDtoService = questionDtoService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
        this.tagDtoService = tagDtoService;
        this.questionViewedService = questionViewedService;
        this.bookmarksService = bookmarksService;
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
        Optional<Long> countQuestion = questionService.getCountByQuestion();
        return new ResponseEntity<>(countQuestion, HttpStatus.OK);
    }

    @GetMapping("api/user/question/{questionId}/comment")
    @Operation(summary = "Получить список комементариев к вопросу")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentQuestion.class))
    })
    @ApiResponse(responseCode = "400", description = "Неверные учетные данные", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<List<QuestionCommentDto>> getQuestionIdComment(@PathVariable("questionId") Long questionId) {
        List<QuestionCommentDto> questionIdComment = questionDtoService.getQuestionByIdComment(questionId);
        return new ResponseEntity<>(questionIdComment, HttpStatus.OK);
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

    @Operation(
            summary = "Добавление вопроса",
            description = "Добавление вопроса"
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
    }


    @GetMapping("api/user/question/tag/{id}")
    @Operation(
            summary = "Получение списка вопросов по tag id",
            description = "Получение пагинированного списка dto вопросов по id тэга"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает пагинированный список QuestionDto " +
                    "(id, title, authorId, authorReputation, authorName, authorImage, description, viewCount," +
                    "countAnswer, countValuable, persistDateTime, lastUpdateDateTime, listTagDto)",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionViewDto.class)
                    )
            }
    )
    public ResponseEntity<PageDTO<QuestionViewDto>> getPageQuestionsByTagId(@PathVariable Long id,
                                                                            @RequestParam int page,
                                                                            @RequestParam(defaultValue = "10") int items) {
        PaginationData data = new PaginationData(
                page, items, QuestionPageDtoDaoByTagId.class.getSimpleName()
        );
        data.getProps().put("id", id);
        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
    }

    @GetMapping("api/user/question/new")
    @Operation(
            summary = "Получение вопросов",
            description = "Сортировка по дате добавления(сначала самые новые)"
    )
    public ResponseEntity<PageDTO<QuestionViewDto>> getQuestionsSortedByDate(@RequestParam int page,
                                                                             @RequestParam(defaultValue = "10") int items,
                                                                             @RequestParam(required = false) List<Long> trackedTag,
                                                                             @RequestParam(required = false) List<Long> ignoredTag) {
        PaginationData data = new PaginationData(page, items,
                QuestionPageDtoDaoSortedByDate.class.getSimpleName());
        data.getProps().put("trackedTag", trackedTag);
        data.getProps().put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
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
    public ResponseEntity<PageDTO<QuestionViewDto>> getQuestionsWithNoAnswer(@RequestParam int page,
                                                                             @RequestParam(required = false, defaultValue = "10") int items,
                                                                             @RequestParam(required = false) List<Long> trackedTag,
                                                                             @RequestParam(required = false) List<Long> ignoredTag) {

        PaginationData data = new PaginationData(page, items, QuestionPageDtoDaoByNoAnswersImpl.class.getSimpleName());
        data.getProps().put("trackedTags", trackedTag);
        data.getProps().put("ignoredTags", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
    }

    @GetMapping("/api/user/question")
    @Operation(summary = "Получение пагинированного списка вопросов с возможностью учета trackedTag и ignoredTag",
            description = "Получение пагинированного списка вопросов пользователя, " +
                    "в запросе указываем page - номер страницы, обязательный параметр, items (по умолчанию 10) - количество результатов на странице," +
                    "не обязательный на фронте, trackedTag - не обязательный параметр, ignoredTag - не обязательный параметр")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionDTO> (id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> allQuestionsWithTrackedTagsAndIgnoredTags(@RequestParam int page, @RequestParam(required = false, defaultValue = "10") int items,
                                                                                              @RequestParam(required = false) List<Long> trackedTag,
                                                                                              @RequestParam(required = false) List<Long> ignoredTag) {
        PaginationData data = new PaginationData(page, items, QuestionPageDtoDaoAllQuestionsImpl.class.getSimpleName());
        data.getProps().put("trackedTags", trackedTag);
        data.getProps().put("ignoredTags", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(
            summary = "Помечает вопрос как прочитанный",
            description = "Помечает вопрос как прочитанный"
    )
    @ApiResponse(responseCode = "200", description = "Метод выполнен без ошибок", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "403", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json")
    })
    @GetMapping("api/user/question/{id}/view")
    public ResponseEntity<String> markQuestionLikeViewed(@PathVariable Long id, Authentication auth) {

        User user = (User) auth.getPrincipal();
        Optional<Question> question = questionService.getById(id);

        if (question.isPresent()) {
            questionViewedService.markQuestionLikeViewed(user, question.get());
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }

        return new ResponseEntity<>("There is no question " + id.toString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/user/popular")
    @Operation(summary = "Получение полного пагинированного списка популярных вопросов",
            description = "Получение пагинированного списка вопросов пользователя, " +
                    "в запросе указываем page - номер страницы, обязательный параметр, items (по умолчанию 10) - количество результатов на странице")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionDTO> (id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> AllQuestionSortedByPopular(@RequestParam int page,
                                                                               @RequestParam(required = false, defaultValue = "10") int items,
                                                                               @RequestParam(required = false) List<Long> trackedTag,
                                                                               @RequestParam(required = false) List<Long> ignoredTag) {

        PaginationData data = new PaginationData(page, items, QuestionPageDtoDaoAllSortedByPopular.class.getSimpleName());
        data.getProps().put("trackedTag", trackedTag);
        data.getProps().put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
    }

    @GetMapping("api/user/question/paginationForWeek")
    @Operation(summary = "Получение пагинированного списка вопросов за неделю по наибольшим голосам,просмотрам и ответам " +
            "с возможностью учета trackedTag и ignoredTag",
            description = "Получение пагинированного списка вопросов за неделю, " +
                    "в запросе указываем page - номер страницы, обязательный параметр, items (по умолчанию 10) - количество результатов на странице," +
                    "не обязательный на фронте, trackedTag - не обязательный параметр, ignoredTag - не обязательный параметр")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionDTO> (id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> paginationForTheWeek(@RequestParam int page, @RequestParam(required = false, defaultValue = "10") int items,
                                                                         @RequestParam(required = false) List<Long> trackedTag,
                                                                         @RequestParam(required = false) List<Long> ignoredTag) {

        PaginationData data = new PaginationData(page, items, QuestionPageDtoDaoSortedByWeightForTheWeekImpl.class.getSimpleName());
        data.getProps().put("trackedTags", trackedTag);
        data.getProps().put("ignoredTags", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);

    }

    @GetMapping("api/user/question/paginationForMonth")
    @Operation(summary = "Получение пагинированного списка вопросов за месяц по наибольшему количеству ответов,голосам," +
            "(?просмотрам)(приоритет соответсвует порядку)",
            description = "Получение пагинированного списка вопросов за месяц, " +
                    "в запросе указываем page - номер страницы, обязательный параметр, items (по умолчанию 10) - " +
                    "количество результатов на странице, так же можно отфильтровать по ignoredTag и trackedTag" +
                    "(указываются как параметр в HTTP запросе списком)")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionViewDTO> " +
            "(id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> paginationForTheMonth(@RequestParam int page,
                                                                          @RequestParam(required = false, defaultValue = "10") int items,
                                                                          @RequestParam(required = false) List<Long>trackedTag,
                                                                          @RequestParam(required = false) List<Long>ignoredTag){
        PaginationData data = new PaginationData(page, items, QuestionPageDtoDaoSortedByImpl.class.getSimpleName());
        data.getProps().put("trackedTags", trackedTag);
        data.getProps().put("ignoredTags", ignoredTag);
        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(
            summary = "Добавление вопроса в закладки",
            description = "Добавление вопроса в закладки"
    )
    @ApiResponse(responseCode = "200", description = "Закладка успешно добавлена", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "По переданному id нет вопроса или закладка уже существует", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "403", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json")
    })
    @GetMapping("api/user/question/{id}/bookmark")
    public ResponseEntity<String> addQuestionInBookmarks(@PathVariable Long id, Authentication auth) {

        User user = (User) auth.getPrincipal();
        Optional<Question> question = questionService.getById(id);

        if (question.isPresent()) {
            bookmarksService.addQuestionInBookmarks(user, question.get());
            return new ResponseEntity<>("Bookmark successfully added", HttpStatus.OK);
        }

        return new ResponseEntity<>("There is no question with id: " + id.toString(), HttpStatus.BAD_REQUEST);
    }
}

