package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.question.Tag;

public interface TagService extends ReadWriteService<Tag, Long> {

    boolean isExistsInIgnoreTagOrTrackedTagByUserId (Long userId, Long tagId);

}
