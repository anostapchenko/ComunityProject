package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.dao.impl.pagination.transformer.QuestionDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoSortedByDate")
public class QuestionPageDtoSortedByDate implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return (List<QuestionDto>) entityManager.createQuery("select q.id, q.title, u.id," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime, (select sum(r.count) from Reputation r where r.author.id =u.id), " +
                        "(select count (a.id) from Question q JOIN Answer a ON a.question.id = q.id)," +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v JOIN Question " +
                        " q ON v.question.id = q.id) from Question q JOIN q.user u" +
                        " where exists (select q.id from Question q1 JOIN q1.tags t " +
                        " where q.id = q1.id and (:trackedTag is NULL or :trackedTag = t.id))" +
                        " and not exists (select q.id from Question q1 JOIN q1.tags t " +
                        " where q.id = q1.id and :ignoredTag = t.id)" +
                        " ORDER BY q.persistDateTime desc")
                .setParameter("trackedTag", properties.getProps().get("trackedTag"))
                .setParameter("ignoredTag", properties.getProps().get("ignoredTag"))
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoResultTransformer())
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(q.id) from Question q" +
                        " where exists (select q.id from Question q1 JOIN q1.tags t " +
                        " where q.id = q1.id and (:trackedTag is NULL or :trackedTag = t.id))" +
                        " and not exists (select q.id from Question q1 JOIN q1.tags t " +
                        " where q.id = q1.id and :ignoredTag = t.id)")
                .setParameter("trackedTag", properties.get("trackedTag"))
                .setParameter("ignoredTag", properties.get("ignoredTag"))
                .getSingleResult();
    }
}
