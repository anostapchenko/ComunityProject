package com.javamentor.qa.platform.models.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Schema(description = "TagDTO - сокращенный тег")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Имя тега")
    private String name;

    @Schema(description = "Дата сохранения тега")
    private LocalDateTime persistDateTime;
}
