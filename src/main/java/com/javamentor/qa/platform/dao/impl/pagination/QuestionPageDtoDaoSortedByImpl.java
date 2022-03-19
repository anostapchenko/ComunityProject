package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.dao.impl.pagination.transformer.QuestionPageDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoDaoSortedByImpl")
public class QuestionPageDtoDaoSortedByImpl implements PageDtoDao<QuestionViewDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager.createQuery("select " +
                        "q.id, q.title, u.id,u.fullName, u.imageLink, q.description, q.persistDateTime,q.lastUpdateDateTime, " +
                        "coalesce((select sum(r.count) from Reputation r where r.author.id=u.id),0), "+
                        "(coalesce((select count(a.id) from Answer a where a.question.id = q.id),0)) as answerCounter, " +
                        "(coalesce((select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id = q.id),0)) as voteCounter " +
                        "from Question q join User u on q.user.id=u.id " +
                        "where ((:trackedTags) IS NULL OR q.id IN (select q.id from Question q join q.tags t where t.id in (:trackedTags))) and" +
                        "((:ignoredTags) IS NULL OR q.id not IN (select q.id from Question q join q.tags t where t.id in (:ignoredTags)))" +
                        "and q.persistDateTime > date_trunc(:time, current_timestamp) " +
                        "order by answerCounter, voteCounter ")
                .setParameter("time",properties.getProps().get("time"))
                .setParameter("trackedTags",properties.getProps().get("trackedTags"))
                .setParameter("ignoredTags",properties.getProps().get("ignoredTags"))
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(Query.class)
                .setResultTransformer(new QuestionPageDtoResultTransformer())
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select distinct count(distinct q.id) from Question q  " +
                        "where ((:trackedTags) is null " +
                        "or q.id in " +
                        "(select q.id from Question q join q.tags t where t.id in (:trackedTags))) and" +
                        "((:ignoredTags) is null " +
                        "or q.id not in " +
                        "(select q.id from Question q join q.tags t where t.id in (:ignoredTags))) and" +
                        " q.persistDateTime > date_trunc(:time, current_timestamp)")
                .setParameter("time",properties.get("time"))
                .setParameter("trackedTags",properties.get("trackedTags"))
                .setParameter("ignoredTags",properties.get("ignoredTags"))
                .getSingleResult();
    }
}
