package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> getListTagsByListOfTagName(List<String> listTagName) {
        String hql = "select tag from Tag tag where tag.name in (:listTagName)";
        Query query = entityManager.createQuery(hql).setParameter("listTagName", listTagName);
        return query.getResultList();
    }

    @Override
    public boolean isExistsInIgnoreTagOrTrackedTagByUserId(Long userId, Long tagId) {
        return entityManager.createQuery(
                        "SELECT CASE " +
                                "WHEN EXISTS (SELECT i.ignoredTag FROM IgnoredTag i " +
                                "WHERE i.user.id = :userId AND i.ignoredTag.id = :tagId) " +
                                "OR " +
                                "EXISTS (SELECT tr.trackedTag FROM TrackedTag tr " +
                                "WHERE tr.user.id = :userId AND tr.trackedTag.id = :tagId) " +
                                "THEN true " +
                                "ELSE false " +
                                "END " +
                                "FROM Tag t Where t.id = :tagId",
                        Boolean.class)
                .setParameter("userId", userId)
                .setParameter("tagId", tagId)
                .getSingleResult();
    }


}