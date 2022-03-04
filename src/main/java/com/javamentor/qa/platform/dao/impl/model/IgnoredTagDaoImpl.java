package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteIgnoredTagByTagIdAndUserId (Long tagId, Long userId){
        String hql = "delete from IgnoredTag it where it.ignoredTag.id = :tagId and it.user.id = :userId";
        entityManager.createQuery(hql)
                .setParameter("tagId", tagId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public boolean existsByTagIdAndUserId(Long tagId, Long userId){
        return (boolean) entityManager.createQuery(
                "SELECT COUNT(it) > 0 FROM IgnoredTag it " +
                        "WHERE it.ignoredTag.id =: tagId and it.user.id = :userId")
                .setParameter("tagId", tagId)
                .setParameter("userId", userId)
                .getSingleResult();

    }
}