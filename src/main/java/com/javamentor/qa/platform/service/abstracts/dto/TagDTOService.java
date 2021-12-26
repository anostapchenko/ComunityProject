package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.TagDTO;

import java.util.List;

public interface TagDTOService {

    List<TagDTO> getTagsDTOByUserIdFromTrackedTag(Long currentUserId);

}
