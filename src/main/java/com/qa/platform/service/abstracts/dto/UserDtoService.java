package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.UserDto;
import com.qa.platform.models.dto.UserProfileQuestionDto;

import java.util.List;
import java.util.Optional;

public interface UserDtoService extends PageDtoService<UserDto> {
    Optional<UserDto> findUserDtoById(Long id);
    List<UserProfileQuestionDto> getAllUserProfileQuestionDtoById(Long id);

    List<UserProfileQuestionDto> getUserProfileQuestionDtoByUserIdIsDeleted(Long id);
}
