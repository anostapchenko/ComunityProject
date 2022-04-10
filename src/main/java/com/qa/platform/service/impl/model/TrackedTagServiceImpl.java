package com.qa.platform.service.impl.model;

import com.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.qa.platform.models.entity.question.TrackedTag;
import com.qa.platform.service.abstracts.model.TrackedTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {

    private final TrackedTagDao trackedTagDao;

    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao) {
        super(trackedTagDao);
        this.trackedTagDao = trackedTagDao;
    }

    @Override
    @Transactional
    public void deleteTrackedTagByTagIdAndUserId(Long tagId, Long userId){
        trackedTagDao.deleteTrackedTagByTagIdAndUserId(tagId, userId);
    }

    @Override
    public boolean existsByTagIdAndUserId(Long tagId, Long userId) {

        return trackedTagDao.existsByTagIdAndUserId(tagId, userId);
    }

}
