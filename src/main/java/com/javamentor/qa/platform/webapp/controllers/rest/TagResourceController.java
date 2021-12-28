package com.javamentor.qa.platform.webapp.controllers.rest;

<<<<<<< HEAD
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import io.swagger.v3.oas.annotations.Operation;
=======
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
>>>>>>> dev
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
<<<<<<< HEAD
import org.springframework.security.core.context.SecurityContextHolder;
=======
>>>>>>> dev
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

<<<<<<< HEAD
@RestController
@Tag(name = "Tag Resource Controller", description = "Управление сущностями, которые связаны с тэгами")
public class TagResourceController {

    private final
    TagDtoService tagDtoService;

    public TagResourceController(TagDtoService tagDtoService) {
        this.tagDtoService = tagDtoService;
    }


    @GetMapping("/api/user/tag/ignored")
    @Operation(
            summary = "Тэг, который пользователь выбрал для игнорирования",
            description = "Возвращает список тэгов, которые пользователь выбрал для игнорирования"
    )
    public ResponseEntity<?> getIgnoredTag (){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) auth.getPrincipal();
        Long userId = user.getId();
        List<TagDto> listTagDtoByIgnoredTag = tagDtoService.getIgnoredTagByUserId(userId);
        return new ResponseEntity<>(listTagDtoByIgnoredTag, HttpStatus.OK);
=======
@Tag(name = "TagResourceController", description = "Позволяет работать с тегами")
@RestController
public class TagResourceController {

    private TagDtoService tagDtoService;

    @Autowired
    public void setTagServiceImpl(TagDtoService tagDtoService) {
        this.tagDtoService = tagDtoService;
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
    @GetMapping(path = "/api/user/tag/tracked")
    public ResponseEntity<List<TagDto>>  getTagDto(Authentication authentication) {
        User currentUser = (User)authentication.getPrincipal();
        return new ResponseEntity<>(tagDtoService.getTrackedTagsByUserId(currentUser.getId()),
                HttpStatus.OK);
>>>>>>> dev
    }
}
