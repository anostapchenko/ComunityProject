package com.javamentor.qa.platform.models.dto.question;


import com.javamentor.qa.platform.models.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Тег с количеством вопросов по нему")
public class PopularTagDto extends TagDto {

    @Schema(description = "Количество вопросов по тегу")
    private Long countQuestion;
}
