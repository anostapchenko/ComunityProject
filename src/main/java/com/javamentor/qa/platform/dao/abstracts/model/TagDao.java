package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface TagDao extends ReadWriteDao<Tag, Long> {
    List<Tag> getListTagsByListOfTagName(List<String> listTagName);

    boolean existsInIgnoreTagOrTrackedTagByUserId (Long userId, Long tagId);
}
