package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteTrackedTagByTagId (Long tagId){
        String hql = "delete from TrackedTag tt where tt.trackedTag.id = :tagId";
        entityManager.createQuery(hql).setParameter("tagId", tagId).executeUpdate();
    }

    @Override
    public boolean existsByTagId(Long tagId){
        long count = (long) entityManager.createQuery("SELECT COUNT(tt) FROM TrackedTag tt WHERE tt.trackedTag.id =: tagId").setParameter("tagId", tagId).getSingleResult();
        return count > 0;
    }
}