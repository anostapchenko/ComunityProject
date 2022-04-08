package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.userdto.UserPageDtoDaoAllUsersByRepImpl;
import com.javamentor.qa.platform.dao.impl.pagination.userdto.UserPageDtoDaoAllUsersImpl;
import com.javamentor.qa.platform.dao.impl.pagination.userdto.UserPageDtoDaoByVoteImpl;
import com.javamentor.qa.platform.models.dto.BookMarksDto;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.BookMarksDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "User Resource Controller", description = "The User API")
public class UserResourceController {

    private final UserDtoService userDtoService;
    private final UserService userService;
    private final BookMarksDtoService bookMarksDtoService;

    public UserResourceController(UserDtoService userDtoService,
                                  UserService userService, BookMarksDtoService bookMarksDtoService) {
        this.userDtoService = userDtoService;
        this.userService = userService;
        this.bookMarksDtoService = bookMarksDtoService;
    }

    @GetMapping("/api/user/{userId}")
    @Operation(summary = "Получение dto пользователя по id",
            description = "Получение null сли пользователь не найден")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список UserDto (id, email, fullName, imageLink, city, reputacion)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })

    public ResponseEntity<UserDto> getUserDtoId(@PathVariable("userId") long id) {

        Optional<UserDto> userDto = userDtoService.findUserDtoById(id);

        if (userDto.isEmpty()) {
            return new ResponseEntity("Пользователь не найден!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDto.get(), HttpStatus.OK);
    }

    @Operation(summary = "Получение пагинированного списка всех пользователей. " +
            "В запросе указываем page - номер страницы, items (по умолчанию 10) - количество результатов на странице",
            description = "Получение пагинированного списка всех пользователей отсортированных по дате создания без учета удаленных пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает пагинированный список PageDTO<UserDto> (id, email, fullName, imageLink, city, reputation)",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })

    @GetMapping("/api/user/new")
    public ResponseEntity<PageDTO<UserDto>> paginationById(@RequestParam int page, @RequestParam(defaultValue = "10") int items) {
        PaginationData data = new PaginationData(page, items,
                UserPageDtoDaoAllUsersImpl.class.getSimpleName());
        return new ResponseEntity<>(userDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(summary = "Постраничное получение списка пользователей",
            description = "Постраничное получение списка пользователей отсортированных по сумме голосов" +
                    "за ответы и вопросы, где DownVote = -1 и UpVote = 1")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает PageDto с вложенным массивом UserDto согласно текущей страницы" +
                            "и количеству запрашиваемых пользователей",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PageDTO.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "400", description = "Неверная нумерация страниц")
    })
    @GetMapping(path = "/api/user/vote")
    public ResponseEntity<PageDTO<UserDto>> getUsersByVoteAsc(@RequestParam(defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "10") Integer items) {
        PaginationData data = new PaginationData(page, items,
                UserPageDtoDaoByVoteImpl.class.getSimpleName());
        return new ResponseEntity<>(userDtoService.getPageDto(data), HttpStatus.OK);
    }

    @PatchMapping("/api/user/change/password")
    @Operation(summary = "Изменение пароля пользователя",
            description = "Пароль должен состоять из букв и цифр, " +
                    "должен быть длинее 6 символов и " +
                    "не должен совпадать с текущим паролем")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изменеяет пароль ",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public ResponseEntity<?> changePassword(@RequestParam String password) {
        userService.changePassword(password,
                userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Получение пагинированного списка всех пользователей. " +
            "В запросе указываем page - номер страницы, items (по умолчанию 10) - количество результатов на странице",
            description = "Получение пагинированного списка всех пользователей отсортированных по репутации без учета удаленных пользователей (аттрибут isDeleted=false)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает пагинированный список PageDTO<UserDto> (id, nickname, imageLink, city, reputation)",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    @GetMapping("/api/user/reputation")
    public ResponseEntity<PageDTO<UserDto>> getAllUserPaginationByReputation(@RequestParam Integer page, @RequestParam(required = false, defaultValue = "10") Integer items) {
        PaginationData data = new PaginationData(page, items,
                UserPageDtoDaoAllUsersByRepImpl.class.getSimpleName());
        return new ResponseEntity<>(userDtoService.getPageDto(data), HttpStatus.OK);
    }
    @Operation(summary = "Получение всех вопросов авторизированного пользователя неотсортированных" +
            "В запросе нет параметров,возвращается список обьектов UserProfileQuestionDto ",
            description = "Получение всех вопросов авторизированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список UserProfileQuestionDto(long questionId,String title, List<TagDto>, Long answerCount, LocalDateTime persistDateTime)",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    @GetMapping("/api/user/profile/questions")
    public ResponseEntity<List<UserProfileQuestionDto>> getAllUserProfileQuestionDtoById(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(userDtoService.getAllUserProfileQuestionDtoById(user.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Получение всех удаленных вопросов в виде UserProfileQuestionDto по email авторизованного пользователя " +
            "Параметры запроса не требуются",
            description = "Получение списка UserProfileQuestionDto на основе вопросов авторизованного пользователя,которые имеют статус isDeleted ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список List<UserProfileQuestionDto> (questionId, title, listTagDto, countAnswer, persistDateTime)",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    @GetMapping("/api/user/profile/delete/questions")
    public ResponseEntity<List<UserProfileQuestionDto>> getAllUserProfileQuestionDtoByUserIdIsDelete(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(userDtoService
                .getUserProfileQuestionDtoByUserIdIsDeleted(user.getId()),
                HttpStatus.OK);
    }

    @Operation(summary = "Получение всех закладок в профиле пользователя в виде BookMarksDto" +
            "Параметры запроса не требуются",
            description = "Получение всех закладок в профиле пользователя в виде BookMarksDto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список List<BookMarksDto> (questionId, title, listTagDto, countAnswer, countVote, countView, persistDateTime)",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    @GetMapping("/api/user/profile/bookmarks")
    public ResponseEntity<List<BookMarksDto>> getAllBookMarksInUserProfile(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(bookMarksDtoService
                .getAllBookMarksInUserProfile(user.getId()),
                HttpStatus.OK);
    }

}
