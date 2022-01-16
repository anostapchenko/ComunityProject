package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/tag")
@Tag(name = "Tag Resource Controller", description = "Управление сущностями, которые связаны с тегами")
public class TagResourceController {

    private final TagDtoService tagDtoService;

    @Operation(
            summary = "Теги, которые пользователь выбрал для игнорирования",
            description = "Возвращает список тегов, которые пользователь выбрал для игнорирования"
    )
    @GetMapping("/ignored")
    public ResponseEntity<?> getIgnoredTag(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return new ResponseEntity<>(tagDtoService.getIgnoredTagsByUserId(user.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка пользовательских тегов",
            description = "Получение списка тегов текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список TagDTO (id, name, persist_date)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/tracked")
    public ResponseEntity<List<TagDto>> getTagDto(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return new ResponseEntity<>(tagDtoService.getTrackedTagsByUserId(currentUser.getId()),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Теги с наибольшим количеством вопросов",
            description = "Возвращает список из максимум 10 тегов, отсортированный по убыванию количества вопросов по ним."
    )
    @GetMapping("/popular")
    public ResponseEntity<List<PopularTagDto>> getTopPopularTags() {
        return new ResponseEntity<>(tagDtoService.getPopularTags(10),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Тэги в наименовании которых встречается строка",
            description = "Возвращает список из максимум 10 тегов в наименовании которых встречается строка.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список TagDTO (id, name, persist_date)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/latter")
    public ResponseEntity<List<TagDto>> getTagsLike(@RequestParam String value) {
        return new ResponseEntity<>(tagDtoService.getTagsLike(value),
                HttpStatus.OK);
    }
}
