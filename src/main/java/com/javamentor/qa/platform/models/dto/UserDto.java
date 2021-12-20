package com.javamentor.qa.platform.models.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String linkImage;
    private String city;
    private int reputation;

    public UserDto(Long id, String email, String fullName, String linkImage, String city, int reputation) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.linkImage = linkImage;
        this.city = city;
        this.reputation = reputation;
    }

}
