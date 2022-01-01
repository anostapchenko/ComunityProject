package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagDtoServiceImpl implements TagDtoService {

    private final TagDtoDao tagDtoDao;

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
}
