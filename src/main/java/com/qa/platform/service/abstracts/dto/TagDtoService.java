package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.question.PopularTagDto;
import com.qa.platform.models.dto.TagDto;
import com.qa.platform.models.dto.question.TagViewDto;

import java.util.List;

public interface TagDtoService extends PageDtoService<TagViewDto>{
    List<TagDto> getIgnoredTagsByUserId(Long userId);
    List<TagDto> getTrackedTagsByUserId(Long userId);
    List<PopularTagDto> getPopularTags();
    List<PopularTagDto> getPopularTags(Integer limit);
    List<TagDto> getTagDtoServiceById(Long id);
    List<TagDto> getTagsLike(String value);
}
