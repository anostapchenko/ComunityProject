package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDTODao;
import com.javamentor.qa.platform.models.dto.question.TagDTO;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDTODaoImpl implements TagDTODao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDTO> getTagDTOListByUserIdFromTrackedTag(Long currentUserId) {
        return entityManager.createQuery(
                "SELECT t.id as id, t.name as name, t.persistDateTime as persistDateTime " +
                        "FROM Tag t JOIN TrackedTag tr " +
                        "ON tr.trackedTag.id = t.id " +
                        "WHERE tr.user.id = :currentUserId"
        )
                .setParameter("currentUserId", currentUserId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(TagDTO.class))
                .getResultList();
    }
}
