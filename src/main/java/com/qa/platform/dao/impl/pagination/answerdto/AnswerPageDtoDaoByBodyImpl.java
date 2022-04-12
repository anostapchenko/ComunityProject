package com.qa.platform.dao.impl.pagination.answerdto;

import com.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.qa.platform.models.dto.AnswerDTO;
import com.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("AnswerPageDtoDaoByBodyImpl")
public class AnswerPageDtoDaoByBodyImpl implements PageDtoDao<AnswerDTO> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerDTO> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager
                .createQuery("SELECT new com.qa.platform.models.dto.AnswerDTO(" +
                        " a.id, a.user.id, (SELECT sum(r.count) FROM Reputation r where r.answer.user.id = a.user.id), " +
                        " a.question.id, a.htmlBody, a.persistDateTime, a.isHelpful, a.dateAcceptTime, " +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end)  from VoteAnswer v  where v.answer.id = a.id )," +
                        " a.user.imageLink, a.user.nickname) " +
                        " FROM Answer as a" +
                        " order by a.htmlBody", AnswerDTO.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(a.id) from Answer a")
                .getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
