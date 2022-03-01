package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.tagdto.TagPageDtoDaoAllTagsByNameImpl;
import com.javamentor.qa.platform.dao.impl.pagination.tagdto.TagPageDtoDaoAllTagsByPersistDateTimeImpl;
import com.javamentor.qa.platform.dao.impl.pagination.tagdto.TagPageDtoDaoAllTagsByPopularImpl;
import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.question.TagViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/tag")
public class TagResourceController {

    private final TagDtoService tagDtoService;
    private final IgnoredTagService ignoredTagService;
    private final TrackedTagService trackedTagService;
    private final TagService tagService;
    private final TagConverter tagConverter;
    private final EntityManager entityManager;


    @Operation(
            summary = "Теги, которые пользователь выбрал для игнорирования",
            description = "Возвращает список тегов, которые пользователь выбрал для игнорирования"
    )
    @GetMapping("/ignored")
    public ResponseEntity<?> getIgnoredTag(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return new ResponseEntity<>(tagDtoService.getIgnoredTagsByUserId(user.getId()), HttpStatus.OK);
    }

    @Operation(
            summary = "Добавление тега, который пользователь выбрал для игнорирования",
            description = "Добавляет тэг для игнорирования в таблицу tag_ignore. " +
                    "Метод ничего не возвращает."
    )
    @PostMapping("/ignored/add")
    public ResponseEntity<?> addIgnoredTag (Authentication auth,
                               @RequestParam(value = "tag") Long id) {
        User user = (User) auth.getPrincipal();
        Tag tag = tagService.getById(id)
                        .orElseThrow(() -> new ConstrainException("Can't find tag with id: " + id));

//        boolean flag = entityManager.createQuery(
//                        "SELECT CASE " +
//                                "WHEN EXISTS (SELECT i.ignoredTag FROM i " +
//                                "WHERE i.user.id = :userId AND i.ignoredTag.id = :tagId) " +
//                                "OR " +
//                                "EXISTS (SELECT tr.trackedTag FROM tr " +
//                                "WHERE tr.user.id = :userId AND tr.trackedTag.id = :tagId) " +
//                                "THEN true " +
//                                "ELSE false " +
//                                "END " +
//                                "FROM IgnoredTag i, " +
//                                "TrackedTag  tr",
//                        Boolean.class)
//                .setParameter("userId", user.getId())
//                .setParameter("tagId", id)
//                .getSingleResult();

        TagDto tagDto = tagConverter.tagToTagDto(tag);
        List<TagDto> ignoredTagDtoList = tagDtoService.getIgnoredTagsByUserId(user.getId());
        List<TagDto> trackedTagDtoList = tagDtoService.getTrackedTagsByUserId(user.getId());

        if (!ignoredTagDtoList.contains(tagDto) && !trackedTagDtoList.contains(tagDto)) {

//        if (!flag) {
            ignoredTagService.persist(new IgnoredTag(tag, user));
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Ignored tag already exists or is contained in the tracked tags",
                HttpStatus.BAD_REQUEST);
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
            summary = "Добавление тега, который пользователь выбрал для отслеживания",
            description = "Добавляет тэг для отслеживания в таблицу tag_tracked. " +
                    "Метод ничего не возвращает."
    )
    @PostMapping("/tracked/add")
    public ResponseEntity<?> addTrackedTag (Authentication auth,
                               @RequestParam(value = "tag") Long id) {
        User user = (User) auth.getPrincipal();
        Tag tag = tagService.getById(id)
                .orElseThrow(() -> new ConstrainException("Can't find tag with id:" + id));
        TagDto tagDto = tagConverter.tagToTagDto(tag);
        List<TagDto> ignoredTagDtoList = tagDtoService.getIgnoredTagsByUserId(user.getId());
        List<TagDto> trackedTagDtoList = tagDtoService.getTrackedTagsByUserId(user.getId());
        if (!ignoredTagDtoList.contains(tagDto) && !trackedTagDtoList.contains(tagDto)) {
            trackedTagService.persist(new TrackedTag(tag, user));
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Tracked tag already exists or is contained in the ignored tags",
                HttpStatus.BAD_REQUEST);
    }

    @Operation(
            summary = "Теги с наибольшим количеством вопросов",
            description = "Возвращает список из максимум 10 тегов, отсортированный по убыванию количества вопросов по ним."
    )
    @GetMapping("/related")
    public ResponseEntity<List<PopularTagDto>> getRelatedTop10Tags() {
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

    @Operation(summary = "Получение пагинированного списка всех тегов по имени",
            description = "Получение пагинированного списка всех тегов отсортированных по имени")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает пагинированный список <PageDTO<TagDto>> (id, name, persist_date, countQuestion, questionCountOneDay, questionCountWeekDay)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PageDTO.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/name")
    public ResponseEntity<PageDTO<TagViewDto>> getAllTagPaginationByName(@RequestParam Integer page, @RequestParam(required = false, defaultValue = "10") Integer items) {
        PaginationData data = new PaginationData(page, items,
                TagPageDtoDaoAllTagsByNameImpl.class.getSimpleName());
        return new ResponseEntity<>(tagDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(summary = "Получение пагинированного списка всех тегов по дате",
            description = "Получение пагинированного списка всех тегов отсортированных дате добавления")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает пагинированный список <PageDTO<TagDto>> (id, name, persist_date, countQuestion, questionCountOneDay, questionCountWeekDay)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PageDTO.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/new")
    public ResponseEntity<PageDTO<TagViewDto>> getAllTagPaginationByPersistDateTime(@RequestParam Integer page, @RequestParam(required = false, defaultValue = "10") Integer items) {
        PaginationData data = new PaginationData(page, items,
                TagPageDtoDaoAllTagsByPersistDateTimeImpl.class.getSimpleName());
        return new ResponseEntity<>(tagDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(summary = "Получение пагинированного списка всех тегов по популярности",
            description = "Получение пагинированного списка всех тегов отсортированных по популярности." +
                    "Популярность тэгов определяется количеством вопросов за этим тэгом, чем больше за этим тэгом вопросов тем он популярнее.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает пагинированный список <PageDTO<TagDto>> (id, name, persist_date, countQuestion, questionCountOneDay, questionCountWeekDay)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PageDTO.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/popular")
    public ResponseEntity<PageDTO<TagViewDto>> getAllTagPaginationByPopular(@RequestParam Integer page, @RequestParam(required = false, defaultValue = "10") Integer items) {
        PaginationData data = new PaginationData(page, items,
                TagPageDtoDaoAllTagsByPopularImpl.class.getSimpleName());
        return new ResponseEntity<>(tagDtoService.getPageDto(data), HttpStatus.OK);
    }
}
