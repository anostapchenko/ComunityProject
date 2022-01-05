package com.javamentor.qa.platform.service.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.user.UserDto;
import com.javamentor.qa.platform.service.abstracts.pagination.UserPageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDtoServiceImpl extends DtoServiceImpl<UserDto> implements UserPageDtoService {

    @Autowired
    public UserDtoServiceImpl(Map<String, PageDtoDao<UserDto>> daoMap) {
        super(daoMap);
    }

}
