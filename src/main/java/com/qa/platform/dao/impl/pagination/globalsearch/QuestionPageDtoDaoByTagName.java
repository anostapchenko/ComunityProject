package com.qa.platform.dao.impl.pagination.globalsearch;

import com.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoDaoByTagName")
public class QuestionPageDtoDaoByTagName implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getPaginationItems(PaginationData properties) {
        String tagName = (String) properties.getProps().get("q");
        List<String> tagsList = new ArrayList<>();
        List<String> ignoreTags = new ArrayList<>();

        for(String s : Arrays.asList(tagName.split("\\+"))){
            s = s.replaceAll("\\[|]","");
            if(s.startsWith("-")){
                ignoreTags.add(s.substring(1));
            } else {
                tagsList.add(s);
            }
        }

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
                        " WHERE ((:tags) IS NULL OR q.id IN (select q.id from Question q join q.tags t where" +
                        " t.name in (:tags) GROUP BY q.id HAVING COUNT(*) = (:tagsSize))) AND" +
                        " ((:ignoreTags) IS NULL OR q.id not IN (select q.id from Question q join q.tags t where" +
                        " t.name in (:ignoreTags)))" +
                        " ORDER BY q.persistDateTime desc"
                )
                .setParameter("tags", tagsList)
                .setParameter("tagsSize",(long)tagsList.size())
                .setParameter("ignoreTags", ignoreTags)
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


        return (Long) entityManager.createQuery("select distinct count(distinct q.id) from Question q join q.tags t WHERE " +
                        "((:tags) IS NULL OR t.name IN (:tags)) AND " +
                        "((:ignoreTags) IS NULL OR t.name not IN (:ignoreTags))"
                )
                .setParameter("tags", properties.get("tags"))
                .setParameter("ignoreTags", properties.get("ignoreTags"))
                .getSingleResult();
    }
}
