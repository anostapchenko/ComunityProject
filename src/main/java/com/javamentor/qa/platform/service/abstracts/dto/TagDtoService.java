package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

import java.util.List;

public interface TagDtoService extends ReadWriteService<TagDto, Long> {

    List<TagDto> getIgnoredTagByUserId(Long userId);
    List<TagDto> getTrackedTagsByUserId(Long currentUserId);
}
