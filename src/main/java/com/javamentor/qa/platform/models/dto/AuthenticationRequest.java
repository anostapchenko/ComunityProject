package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    @Schema(description = "Имя пользователя", example = "user1@mail.ru")
    private String username;
    @Schema(description = "Пароль", example = "user1")
    private String password;
    @Schema(description = "Запомни меня", example = "true")
    private Boolean rememberMe;
    {
        rememberMe = false;
    }
}
