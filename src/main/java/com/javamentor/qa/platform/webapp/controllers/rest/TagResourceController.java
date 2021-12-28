package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    }
}
