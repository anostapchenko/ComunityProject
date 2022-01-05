package com.javamentor.qa.platform.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String fullName;
    private String imageLink;
    private String city;
    private Long reputation;

}
