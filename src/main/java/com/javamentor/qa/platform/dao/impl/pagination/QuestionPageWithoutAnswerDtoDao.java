package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionPageWithoutAnswerDtoDao implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        List<QuestionDto> questionDtos =  entityManager.createQuery("select q.id, q.title, u.id," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime, (select sum(r.count) from Reputation r where r.author.id =u.id), " +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v JOIN Question q" +
                        " ON v.question.id = q.id) from Question q JOIN q.user u LEFT OUTER JOIN q.answers")
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuple, String[] strings) {
                        QuestionDto questionDto = new QuestionDto();
                        questionDto.setId((Long) tuple[0]);
                        questionDto.setTitle((String) tuple[1]);
                        questionDto.setAuthorId((Long) tuple[2]);
                        questionDto.setAuthorName((String) tuple[3]);
                        questionDto.setAuthorImage((String) tuple[4]);
                        questionDto.setDescription((String) tuple[5]);
                        questionDto.setPersistDateTime((LocalDateTime) tuple[6]);
                        questionDto.setLastUpdateDateTime((LocalDateTime) tuple[7]);
                        questionDto.setAuthorReputation((Long) tuple[8]);
                        questionDto.setCountAnswer(0);
                        questionDto.setCountValuable(((Number) tuple[9]).intValue());
                        return questionDto;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
                .list();

        return questionDtos;
    }

    @Override
    public Long getTotalResultCount() {
        return (Long) entityManager.createQuery("select count(q.id) from Question q LEFT OUTER JOIN q.answers").getSingleResult();
    }
}
