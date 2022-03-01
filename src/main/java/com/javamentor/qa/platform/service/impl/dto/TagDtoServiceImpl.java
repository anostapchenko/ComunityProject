package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.question.TagViewDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class TagDtoServiceImpl extends DtoServiceImpl<TagViewDto> implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    public TagDtoServiceImpl(Map<String, PageDtoDao<TagViewDto>> daoMap, TagDtoDao tagDtoDao) {
        super(daoMap);
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<TagDto> getIgnoredTagsByUserId(Long userId){
        return tagDtoDao.getIgnoredTagsByUserId(userId);
    }

    @Override
    public List<TagDto> getTrackedTagsByUserId(Long currentUserId) {
        return tagDtoDao.getTrackedTagsByUserId(currentUserId);
    }

    @Override
    public List<PopularTagDto> getPopularTags() {
        return tagDtoDao.getPopularTags();
    }

    @Override
    public List<PopularTagDto> getPopularTags(Integer limit) {
        return tagDtoDao.getPopularTags(limit);
    }

    @Override
    public List<TagDto> getTagDtoServiceById(Long id) {
        return tagDtoDao.getTagDtoDaoById(id);
    }

    @Override
    public List<TagDto> getTagsLike(String value){
        return tagDtoDao.getTagsLike(value);
    }
}
