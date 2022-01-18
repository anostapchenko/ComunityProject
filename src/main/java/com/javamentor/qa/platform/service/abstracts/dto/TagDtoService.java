package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TagDtoService {
    List<TagDto> getIgnoredTagsByUserId(Long userId);
    List<TagDto> getTrackedTagsByUserId(Long userId);
    List<PopularTagDto> getPopularTags();
    List<PopularTagDto> getPopularTags(Integer limit);
    List<TagDto> getTagDtoServiceById(Long id);
    List<TagDto> getTagsLike(String value);
}
