package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import org.springframework.stereotype.Service;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {

    private final TrackedTagDao trackedTagDao;

    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao) {
        super(trackedTagDao);
        this.trackedTagDao = trackedTagDao;
    }

}
