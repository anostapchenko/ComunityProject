package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import java.util.List;
import java.util.Map;

public interface TagDtoDao {
    List<TagDto> getIgnoredTagsByUserId(Long userId);
    List<TagDto> getTrackedTagsByUserId(Long userId);
    List<PopularTagDto> getPopularTags();
    List<PopularTagDto> getPopularTags(Integer limit);
    List<TagDto> getTagDtoDaoById(Long id);
    Map<Long, List<TagDto>> getTagDtoByQuestionsId(List<Long> questionsIds);
    List<TagDto> getTagsLike(String value);
    Map<Long, List<TagDto>> getTagDtoDaoByQuestionIds(List<Long> questionIds);
}

