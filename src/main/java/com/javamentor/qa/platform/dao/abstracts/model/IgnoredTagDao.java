package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.IgnoredTag;


public interface IgnoredTagDao extends ReadWriteDao<IgnoredTag, Long> {

    void deleteIgnoredTagByTagId (Long tagId);

    boolean existsByTagId(Long tagId);

}
