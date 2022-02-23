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
    public void deleteIgnoredTagByTagId (Long tagId){
        String hql = "delete from IgnoredTag it where it.ignoredTag.id = :id";
        entityManager.createQuery(hql).setParameter("id", tagId).executeUpdate();
    }

    @Override
    public boolean existsByTagId(Long tagId){
        long count = (long) entityManager.createQuery("SELECT COUNT(it) FROM IgnoredTag it WHERE it.ignoredTag.id =: tagId").setParameter("tagId", tagId).getSingleResult();
        return count > 0;
    }
}