package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.TrackedTag;

public interface TrackedTagService extends ReadWriteService<TrackedTag, Long> {

    void deleteTrackedTagByTagId (Long tagId);
}
