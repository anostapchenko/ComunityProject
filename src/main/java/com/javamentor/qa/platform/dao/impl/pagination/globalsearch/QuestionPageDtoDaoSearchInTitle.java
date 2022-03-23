package com.javamentor.qa.platform.dao.impl.pagination.globalsearch;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoDaoSearchInTitle")
public class QuestionPageDtoDaoSearchInTitle implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager.createQuery(
                        "select " +
                                " q.id, q.title, u.id, (select sum(r.count) from Reputation r where r.author.id =q.user.id)," +
                                " u.fullName, u.imageLink, q.description,0 as viewCount," +
                                " (select count (a.id) from Answer a where a.question.id = q.id)," +
                                " (select count(vq.id) from VoteQuestion vq where vq.question.id=q.id)," +
                                " q.persistDateTime, q.lastUpdateDateTime" +
                                " from Question q JOIN q.user u" +
                                " WHERE lower(q.title) like lower(concat('%',:q,'%'))" +
                                " ORDER BY q.persistDateTime desc"
                )
                .setParameter("q",((String) properties.getProps().get("q")).replaceAll("title:",""))
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public QuestionViewDto transformTuple(Object[] tuple, String[] strings) {
                        QuestionViewDto questionViewDto = new QuestionViewDto();
                        questionViewDto.setId((Long) tuple[0]);
                        questionViewDto.setTitle((String) tuple[1]);
                        questionViewDto.setAuthorId((Long) tuple[2]);
                        questionViewDto.setAuthorReputation((Long) tuple[3]);
                        questionViewDto.setAuthorName((String) tuple[4]);
                        questionViewDto.setAuthorImage((String) tuple[5]);
                        questionViewDto.setDescription((String) tuple[6]);
                        questionViewDto.setViewCount(((Number) tuple[7]).intValue());
                        questionViewDto.setCountAnswer(((Number) tuple[8]).intValue());
                        questionViewDto.setCountValuable(((Number) tuple[9]).intValue());
                        questionViewDto.setPersistDateTime((LocalDateTime) tuple[10]);
                        questionViewDto.setLastUpdateDateTime((LocalDateTime) tuple[11]);
                        return questionViewDto;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {

        return (Long) entityManager.createQuery("select distinct count(distinct q.id) from Question q WHERE " +
                        "lower(q.title) like lower(concat('%',:q,'%'))"
                )
                .setParameter("q", ((String)properties.get("q")).replaceAll("title:",""))
                .getSingleResult();
    }
}
