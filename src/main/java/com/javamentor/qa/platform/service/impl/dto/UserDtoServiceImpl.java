package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserDtoServiceImpl extends DtoServiceImpl<UserDto> implements UserDtoService {
    private final UserDtoDao userDtoDao;

    public UserDtoServiceImpl(UserDtoDao userDtoDao, Map<String, PageDtoDao<UserDto>> daoMap) {
        super(daoMap);
        this.userDtoDao = userDtoDao;
    }

    @Override
    public Optional<UserDto> findUserDtoById(Long id) {
        return userDtoDao.findUserDto(id);
    }
}
