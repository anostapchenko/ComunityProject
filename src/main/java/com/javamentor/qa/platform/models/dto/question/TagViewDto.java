package com.javamentor.qa.platform.models.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "TagViewDto - дання ДТО исползуется для пагинации тегов")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagViewDto {

    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Имя тега")
    private String name;

    @Schema(description = "Дата сохранения тега")
    private LocalDateTime persistDateTime;

    @Schema(description = "Описание тега")
    private String description;

    @Schema(description = "Количество вопросов по тегу")
    private Long countQuestion;

    @Schema(description = "Количество вопросов по тегу за день")
    private Long questionCountOneDay;

    @Schema(description = "Количество вопросов по тегу за неделю")
    private Long questionCountWeekDay;

//    public TagViewDto(Long id, String name, LocalDateTime persistDateTime) {
//        this.id = id;
//        this.name = name;
//        this.persistDateTime = persistDateTime;
//    }
}
