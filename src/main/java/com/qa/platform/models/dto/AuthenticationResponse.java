package com.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9ST0xFMiIsInN1YiI6InVzZXIxQG1haWwucnUiLCJpYXQiOjE2Mzk2NTcyNDcsImV4cCI6MTYzOTc0MzY0N30.1gSisAquvj2Taaqd-4wSI5xHMtjje_Pi4IcpOej-Ecg")
    private String token;

}
