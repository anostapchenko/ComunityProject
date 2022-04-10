package com.qa.platform.dao.impl.pagination;


import com.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.qa.platform.dao.impl.pagination.transformer.QuestionPageDtoResultTransformer;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoDaoAllQuestionsImpl")
public class QuestionPageDtoDaoAllQuestionsImpl implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return (List<QuestionViewDto>) entityManager.createQuery("SELECT DISTINCT q.id, q.title, u.id," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime, (SELECT SUM(r.count) from Reputation r WHERE r.author.id = q.user.id), " +
                        " (select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v JOIN Question q" +
                        " ON v.question.id = q.id)" +
                        " from Question q JOIN q.user u JOIN q.tags t " +
                        " WHERE ((:trackedTags) IS NULL OR t.id IN (:trackedTags)) AND" +
                        " ((:ignoredTags) IS NULL OR q.id not IN (select q.id from Question q join q.tags t where t.id in (:ignoredTags)))")
                .setParameter("trackedTags", properties.getProps().get("trackedTags"))
                .setParameter("ignoredTags", properties.getProps().get("ignoredTags"))
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionPageDtoResultTransformer())
                .list();

    }
    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select distinct count(distinct q.id) from Question q join q.tags t WHERE " +
                        "((:trackedTags) IS NULL OR t.id IN (:trackedTags)) AND" +
                        "((:ignoredTags) IS NULL OR q.id NOT IN (SELECT q.id FROM Question q JOIN q.tags t WHERE t.id IN (:ignoredTags)))")
                .setParameter("trackedTags", properties.get("trackedTags"))
                .setParameter("ignoredTags", properties.get("ignoredTags"))
                .getSingleResult();
    }
}
