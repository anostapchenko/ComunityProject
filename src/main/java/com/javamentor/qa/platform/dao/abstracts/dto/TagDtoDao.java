package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.TagDto;

import java.util.List;

public interface TagDtoDao {
    List<TagDto> getTrackedTagsByUserId(Long id);
}
