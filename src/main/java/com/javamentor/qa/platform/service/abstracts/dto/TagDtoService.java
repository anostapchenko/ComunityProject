package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

import java.util.List;
import java.util.Optional;

public interface TagDtoService extends ReadWriteService<TagDto, Long> {

    List<TagDto> getIgnoredTagByUserId(Long userId);
}
