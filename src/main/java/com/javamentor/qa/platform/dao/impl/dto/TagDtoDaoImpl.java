package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTrackedTagsByUserId(Long currentUserId) {
        return entityManager.createQuery(
                "SELECT t.id as id, t.name as name, t.persistDateTime as persistDateTime " +
                        "FROM Tag t JOIN TrackedTag tr " +
                        "ON tr.trackedTag.id = t.id " +
                        "WHERE tr.user.id = :currentUserId"
        )
                .setParameter("currentUserId", currentUserId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(TagDto.class))
                .getResultList();
    }
}
