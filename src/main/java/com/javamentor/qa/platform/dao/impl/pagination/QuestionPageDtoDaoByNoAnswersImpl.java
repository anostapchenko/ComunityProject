package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoDaoByNoAnswersImpl")
public class QuestionPageDtoDaoByNoAnswersImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        List<QuestionDto> questionDtos =  entityManager.createQuery("SELECT q.id, q.title, u.id," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime, (SELECT SUM(r.count) from Reputation r WHERE r.author.id = q.user.id), " +
                        " (select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v JOIN Question q" +
                        " ON v.question.id = q.id)" +
                        " from Question q JOIN q.user u WHERE q.answers IS EMPTY")
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
                        questionDto.setCountValuable(((Number) tuple[9]).intValue());
                        questionDto.setCountAnswer(0);
                        questionDto.setListTagDto(new ArrayList<>());
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
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(q.id) from Question q WHERE q.answers IS EMPTY")
                .getSingleResult();
    }


}
