package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;

import java.util.List;

public interface UserProfileQuestionDtoService {
    List<UserProfileQuestionDto> getUserProfileQuestionDtoIsDeleted(String email);
}
