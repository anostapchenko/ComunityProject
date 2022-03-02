package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteTrackedTagByTagIdAndUserId(Long tagId, Long userId){
        String hql = "delete from TrackedTag tt where tt.trackedTag.id = :tagId and tt.user.id = :userId";
        entityManager.createQuery(hql)
                .setParameter("tagId", tagId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public boolean existsByTagIdAndUserId(Long tagId, Long userId){
        return (boolean) entityManager.createQuery(
                "SELECT COUNT(tt) > 0 FROM TrackedTag tt " +
                        "WHERE tt.trackedTag.id =: tagId and tt.user.id = :userId")
                .setParameter("tagId", tagId)
                .setParameter("userId", userId)
                .getSingleResult();

    }
}