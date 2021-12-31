package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.dto.question.PopularTagDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTagDtoDaoById(Long id) {

        TypedQuery<TagDto> q = entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.question.TagDto(" +
                                "t.id, t.name, t.description, t.persistDateTime)" +
                                " FROM Question q JOIN q.tags t WHERE q.id =: id ", TagDto.class)
                .setParameter("id", id);
        return q.getResultList();
    }
    @Override
    public List<TagDto> getIgnoredTagsByUserId(Long userId) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.question.TagDto(tag.id, tag.name, tag.persistDateTime) " +
                                "from IgnoredTag ignTag inner join ignTag.user " +
                                "left join ignTag.ignoredTag tag where ignTag.user.id = :userId",
                        TagDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<TagDto> getTrackedTagsByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT t.id as id, t.name as name, t.persistDateTime as persistDateTime " +
                                "FROM Tag t JOIN TrackedTag tr " +
                                "ON tr.trackedTag.id = t.id " +
                                "WHERE tr.user.id = :userId"
                )
                .setParameter("userId", userId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(TagDto.class))
                .getResultList();
    }

    @Override
    public List<PopularTagDto> getPopularTags() {
        return popularTagsQuery().getResultList();
    }

    @Override
    public List<PopularTagDto> getPopularTags(Integer limit) {
        return popularTagsQuery().setMaxResults(limit).getResultList();

    }

    private Query popularTagsQuery() {
        return entityManager.createQuery("SELECT " +
                        "t.id as id, t.name as name, t.persistDateTime as persistDateTime, " +
                        "(select count (q.id) from t.questions q) as countQuestion " +
                        "FROM Tag t order by t.questions.size desc"
                )
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(PopularTagDto.class));
    }
}
