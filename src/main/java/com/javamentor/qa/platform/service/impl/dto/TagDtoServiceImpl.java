package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagDtoServiceImpl extends ReadWriteServiceImpl<TagDto, Long> implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        super(tagDtoDao);
        this.tagDtoDao = tagDtoDao;
    }
    @Override
    public List<TagDto> getIgnoredTagByUserId(Long userId){
        return tagDtoDao.getIgnoredTagByUserId(userId);
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
