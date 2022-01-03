package com.javamentor.qa.platform.models.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "UserDto - сокращенные сведения о пользователе")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "email - используется в качестве username для входа в систему")
    private String email;

    @Schema(description = "Имя")
    private String fullName;

    @Schema(description = "Связанная картинка")
    private String linkImage;

    @Schema(description = "Город")
    private String city;

    @Schema(description = "Величина репутации - сумма общей репутации за вопросы и ответы")
    private int reputation;
}

