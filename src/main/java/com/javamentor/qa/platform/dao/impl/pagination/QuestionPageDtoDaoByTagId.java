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

@Repository("QuestionPageDtoDaoByTagId")
public class QuestionPageDtoDaoByTagId implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return (List<QuestionDto>) entityManager.createQuery(
                        "select " +
                                "q.id, q.title, u.id, u.fullName, u.imageLink, " +
                                " q.description, q.persistDateTime, q.lastUpdateDateTime, " +
                                "(select sum(r.count) from Reputation r WHERE r.author.id = u.id), " +
                                "count(a.id), " +
                                "sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) " +
                                "from Question q " +
                                "JOIN q.user u on q.user.id = u.id " +
                                "join Answer a ON a.question.id = q.id " +
                                "JOIN VoteQuestion v  ON v.question.id = q.id " +
                                "WHERE q.id IN (select q1.id from Question q1 join q1.tags t WHERE t.id = :id)" +
                                "group by q.id, q.persistDateTime, u.id " +
                                "order by q.persistDateTime desc")
                .setParameter("id", properties.getProps().get("id"))
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoResultTransformer())
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count (q.id) from Question q Join q.tags t WHERE t.id = :id")
                .setParameter("id", properties.get("id"))
                .getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
