package com.javamentor.qa.platform.service.abstracts.dto;
import com.javamentor.qa.platform.models.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserDtoService {
    Optional<UserDto> findUserDtoById(Long id);

}
