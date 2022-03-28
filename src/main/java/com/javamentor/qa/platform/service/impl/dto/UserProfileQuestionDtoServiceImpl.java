package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileQuestionDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserProfileQuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileQuestionDtoServiceImpl implements UserProfileQuestionDtoService {
    UserProfileQuestionDtoDao userProfileQuestionDtoDao;

    public UserProfileQuestionDtoServiceImpl(UserProfileQuestionDtoDao userProfileQuestionDtoDao) {
        this.userProfileQuestionDtoDao = userProfileQuestionDtoDao;
    }

    @Override
    public List<UserProfileQuestionDto> getUserProfileQuestionDtoIsDeleted(String email) {
        return userProfileQuestionDtoDao.getAllUserProfileQuestionDtoByEmailWhereQuestionIsDeleted(email);
    }
}
