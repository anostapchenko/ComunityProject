package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements TagService {

    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        super(tagDao);
        this.tagDao = tagDao;
    }

    @Override
    public boolean isExistsInIgnoreTagOrTrackedTagByUserId(Long userId, Long tagId) {
        return tagDao.isExistsInIgnoreTagOrTrackedTagByUserId(userId, tagId);
    }
}
