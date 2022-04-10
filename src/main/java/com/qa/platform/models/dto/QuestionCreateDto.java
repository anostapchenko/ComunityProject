package com.qa.platform.models.dto;

import com.qa.platform.models.util.OnCreate;
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

    @Schema(description = "Заголовок вопроса")
    @NotNull(groups = OnCreate.class, message = "Значение title отсутствует")
    @NotBlank(groups = OnCreate.class, message = "Значение title не должно быть пустым")
    @NotNull String title;

    @Schema(description = "Текст вопроса")
    @NotNull(groups = OnCreate.class, message = "Значение description отсутствует")
    @NotBlank(groups = OnCreate.class, message = "Значение description не должно быть пустым")
    @NotNull private String description;

    @Schema(description = "Список тэгов к вопросу")
    @NotNull(groups = OnCreate.class, message = "Значение tags должно быть заполнено")
    @NotBlank(groups = OnCreate.class, message = "Значение tags не должно быть пустым")
    private List<TagDto> tags;
}
