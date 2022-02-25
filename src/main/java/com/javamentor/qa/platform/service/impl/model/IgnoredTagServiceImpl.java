package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IgnoredTagServiceImpl extends ReadWriteServiceImpl<IgnoredTag, Long> implements IgnoredTagService {

    private final IgnoredTagDao ignoredTagDao;

    public IgnoredTagServiceImpl(IgnoredTagDao ignoredTagDao) {
        super(ignoredTagDao);
        this.ignoredTagDao = ignoredTagDao;
    }

    @Override
    @Transactional
    public void deleteIgnoredTagByTagId (Long tagId){
        ignoredTagDao.deleteIgnoredTagByTagId(tagId);
    }

    @Override
    public boolean existsByTagId(Long tagId){
        return ignoredTagDao.existsByTagId(tagId);
    }
}
