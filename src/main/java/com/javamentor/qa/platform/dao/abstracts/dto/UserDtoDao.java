package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;
import java.util.List;

public interface UserDtoDao {
    UserDto findUserDto(Long id);
}
