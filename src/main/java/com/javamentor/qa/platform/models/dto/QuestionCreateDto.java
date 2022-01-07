package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.util.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Создание вопроса")
public class QuestionCreateDto implements Serializable {

    @Schema(description = "Заголовок вопроса", example = "Вопрос по Spring'у")
    @NotNull(groups = OnCreate.class, message = "Значение title отсутствует")
    @NotBlank(groups = OnCreate.class, message = "Значение title не должно быть пустым")
    @NotNull String title;

//    @NotNull(groups = OnCreate.class, message = "Значение userId должно быть заполнено")
//    private Long userId;

    @Schema(description = "Текст вопроса", example = "Как подключить Spring Security к Проекту?")
    @NotNull(groups = OnCreate.class, message = "Значение description отсутствует")
    @NotBlank(groups = OnCreate.class, message = "Значение description не должно быть пустым")
    @NotNull private String description;

    @Schema(description = "Список тэгов к вопросу", example = "Spring Security")
    @NotNull(groups = OnCreate.class, message = "Значение tags должно быть заполнено")
    @NotBlank(groups = OnCreate.class, message = "Значение tags не должно быть пустым")
    private List<TagDto> tags;
}
