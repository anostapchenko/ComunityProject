package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagDtoServiceImpl extends ReadWriteServiceImpl<TagDto, Long> implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        super(tagDtoDao);
        this.tagDtoDao = tagDtoDao;
    }

    public List<TagDto> getIgnoredTagByUserId(Long userId){
        return tagDtoDao.getIgnoredTagByUserId(userId);
    }
}

