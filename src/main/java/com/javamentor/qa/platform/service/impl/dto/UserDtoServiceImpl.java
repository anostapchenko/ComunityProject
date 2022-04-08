package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDtoServiceImpl extends DtoServiceImpl<UserDto> implements UserDtoService {
    private final UserDtoDao userDtoDao;
    private final TagDtoDao tagDtoDao;

    public UserDtoServiceImpl(UserDtoDao userDtoDao, Map<String, PageDtoDao<UserDto>> daoMap, TagDtoDao tagDtoDao) {
        super(daoMap);
        this.userDtoDao = userDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public Optional<UserDto> findUserDtoById(Long id) {
        return userDtoDao.findUserDto(id);
    }
    @Override
    public List<UserProfileQuestionDto> getUserProfileQuestionDtoByUserIdIsDeleted(Long id) {
        List<UserProfileQuestionDto> resultList=userDtoDao.getAllUserProfileQuestionDtoByUserIdWhereQuestionIsDeleted(id);
        var map = tagDtoDao.getTagDtoByQuestionIds(
                resultList.stream().map(UserProfileQuestionDto::getQuestionId).collect(Collectors.toList())
        );
        resultList.forEach(q -> q.setListTagDto(map.containsKey(q.getQuestionId())?map.get(q.getQuestionId()):new ArrayList<>()));
        return resultList;
    }

    @Override
    public List<UserProfileQuestionDto> getAllUserProfileQuestionDtoById(Long id) {
        List<UserProfileQuestionDto> resultList=userDtoDao.getAllUserProfileQuestionDtoById(id);
        var map = tagDtoDao.getTagDtoByQuestionIds(
                resultList.stream().map(UserProfileQuestionDto::getQuestionId).collect(Collectors.toList())
        );
        resultList.forEach(q ->
                q.setListTagDto(map.containsKey(q.getQuestionId())?map.get(q.getQuestionId()):new ArrayList<>()));
        return resultList;
    }
}
