package com.javamentor.qa.platform.dao.abstracts.dto;

import java.util.List;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.question.TagDto;


public interface TagDtoDao extends ReadWriteDao<TagDto, Long>{

    List<TagDto> getIgnoredTagByUserId(Long userId);
    List<TagDto> getTrackedTagsByUserId(Long id);
}

