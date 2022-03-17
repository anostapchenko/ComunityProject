package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.dao.impl.pagination.transformer.QuestionPageDtoResultTransformerCheckNPE;
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
        List<QuestionViewDto> questionViewDtoTypedQuery;
        questionViewDtoTypedQuery = entityManager.createQuery("select " +
                        "q.id, q.title, u.id,u.fullName, u.imageLink, q.description, q.persistDateTime,q.lastUpdateDateTime, " +
                        "(select sum(r.count) from Reputation r where r.author.id=u.id), "+
                        "(select count(a.id) from Answer a where a.question.id = q.id) as answerCount, " +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id = q.id) as voteCount " +
                        "from Question q join User u on q.user.id=u.id " +
                        "where q.persistDateTime > date_trunc('month', current_timestamp) " +
                        "order by answerCount, voteCount ")
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(Query.class)
                .setResultTransformer(new QuestionPageDtoResultTransformerCheckNPE())
                .getResultList();

        return questionViewDtoTypedQuery;
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery(
                        "select count(q.id) from Question q " +
                                "where q.persistDateTime > date_trunc('month', current_timestamp)")
                .getSingleResult();
    }
}
