package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

}