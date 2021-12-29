package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.TagDto;
import java.util.List;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
public interface TagDtoDao extends ReadWriteDao<TagDto, Long>{

    List<TagDto> getIgnoredTagByUserId(Long userId);
    List<TagDto> getTagDtoDaoById(Long id);
    List<TagDto> getTrackedTagsByUserId(Long id);

}

