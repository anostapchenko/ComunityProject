package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.UserPageDtoDaoAllUsersImpl;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.dao.impl.pagination.UserPageDtoDaoByVoteImpl;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.user.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.UserPageDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User Resource Controller", description = "The User API")
public class UserResourceController {


    private final UserPageDtoService userDtoService;

    @Autowired
    public UserResourceController(UserPageDtoService userDtoService) {
        this.userDtoService = userDtoService;
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
    public ResponseEntity<PageDTO<UserDto>> getUsersByVoteAsc(@RequestParam Integer page, @RequestParam(required = false) Integer items) {
        if (page == null || page <= 0) {
            return new ResponseEntity("Incorrect page number", HttpStatus.BAD_REQUEST);
        }
        if (items == null || items <= 0) {
            items = 10;
        }
        PaginationData data = new PaginationData(page, items,
                UserPageDtoDaoByVoteImpl.class.getSimpleName());
        return new ResponseEntity<>(userDtoService.getPageDto(data), HttpStatus.OK);
    }
}
