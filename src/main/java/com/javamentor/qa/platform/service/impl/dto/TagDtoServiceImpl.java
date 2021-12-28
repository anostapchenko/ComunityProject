package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagDtoServiceImpl implements TagDtoService {

    private TagDtoDao tagDtoDao;

    @Autowired
    public void setTagDtoDao(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<TagDto> getTagDtoServiceById(Long id) {
        return tagDtoDao.getTagDtoDaoById(id);
    }

    @Override
    public List<TagDto> getTrackedTagsByUserId(Long currentUserId) {
        return tagDtoDao.getTrackedTagsByUserId(currentUserId);
    }
}
