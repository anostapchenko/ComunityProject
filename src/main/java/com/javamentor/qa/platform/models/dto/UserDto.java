package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDto {
    @Schema(description = "Идентификатор", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "email", example = "test@mail.ru")
    private String email;
    @Schema(description = "Name", example = "User")
    private String fullName;
    @Schema(description = "linkImage", example = "linkImage")
    private String linkImage;
    @Schema(description = "city", example = "Moskow")
    private String city;
    @Schema(description = "Репутация", accessMode = Schema.AccessMode.READ_ONLY)
    private Long reputation;

    public UserDto(Long id, String email, String fullName, String linkImage, String city, Long reputation) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.linkImage = linkImage;
        this.city = city;
        this.reputation = reputation;
    }

}
