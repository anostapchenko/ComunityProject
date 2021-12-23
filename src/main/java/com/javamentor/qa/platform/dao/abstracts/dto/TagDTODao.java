package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.TagDTO;

import java.util.List;

public interface TagDTODao {
    List<TagDTO> getTagDTOListByUserIdFromTrackedTag(Long id);
}
