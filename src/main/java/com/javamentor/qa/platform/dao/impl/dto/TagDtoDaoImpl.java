package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
<<<<<<< HEAD
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
=======
import com.javamentor.qa.platform.models.dto.question.TagDto;
import org.hibernate.transform.Transformers;
>>>>>>> dev
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

@Repository
public class TagDtoDaoImpl extends ReadWriteDaoImpl<TagDto, Long> implements TagDtoDao {
=======

@Repository
public class TagDtoDaoImpl implements TagDtoDao {
>>>>>>> dev

    @PersistenceContext
    private EntityManager entityManager;

    @Override
<<<<<<< HEAD
    public List<TagDto> getIgnoredTagByUserId(Long userId){
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name) " +
                   "from IgnoredTag ignTag inner join ignTag.user  " +
                   "left join ignTag.ignoredTag tag where ignTag.user.id=:userId",
                    TagDto.class)
                .setParameter("userId",userId)
                .getResultList();
    }

=======
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
>>>>>>> dev
}
