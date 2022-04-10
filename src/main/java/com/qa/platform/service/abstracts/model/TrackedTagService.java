package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.question.TrackedTag;

public interface TrackedTagService extends ReadWriteService<TrackedTag, Long> {

    void deleteTrackedTagByTagIdAndUserId(Long tagId, Long userId);

    boolean existsByTagIdAndUserId(Long tagId, Long userId);
}

