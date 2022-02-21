package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.dao.impl.pagination.transformer.QuestionPageDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("QuestionPageDtoDaoAllSortedByPopular")
public class QuestionPageDtoDaoAllSortedByPopular  implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;

        List<QuestionViewDto> list = (List<QuestionViewDto>) entityManager.createNativeQuery(
                 "with " +
                    "    r as (select author_id as author_id, sum(count) as authorReputation from reputation group by author_id), " +
                    "    qv as (select question_id as question_id, sum(1) as viewCount from question_viewed group by question_id), " +
                    "    vq as (select question_id as question_id, " +
                    "                  sum(case when vote = 'DOWN_VOTE' then -1 when vote = 'UP_VOTE' then 1 else 0 end) as count, " +
                    "                  sum(case when vote = 'UP_VOTE' then 1 else 0 end) as countValuable " +
                    "            from votes_on_questions " +
                    "            group by question_id), " +
                    "    a as (select question_id as question_id, sum(1) as countAnswer from answer where not is_deleted and not is_deleted_by_moderator group by question_id) " +
                    "select " +
                    "    q.id, " +
                    "    q.title, " +
                    "    q.user_id as authorId, " +
                    "    COALESCE(r.authorReputation, 0) as authorReputation, " +
                    "    u.full_name as authorName, " +
                    "    u.image_link as authorImage, " +
                    "    q.description, " +
                    "    COALESCE(qv.viewCount, 0) as viewCount, " +
                    "    COALESCE(a.countAnswer, 0) as countAnswer, " +
                    "    COALESCE(vq.countValuable, 0) as countValuable, " +
                    "    q.persist_date as persistDateTime, " +
                    "    q.last_redaction_date as lastUpdateDateTime " +
                    "from " +
                    "    question as q " +
                    "left join r on q.user_id = r.author_id " +
                    "left join qv on q.id = qv.question_id " +
                    "left join vq on q.id = vq.question_id " +
                    "left join a on q.id = a.question_id " +
                    "left join user_entity as u on q.user_id = u.id " +
                    "where not q.is_deleted " +
                    "order by " +
                    "    COALESCE(a.countAnswer, 0) desc, " +
                    "    COALESCE(vq.countValuable, 0) desc, " +
                    "    COALESCE(qv.viewCount, 0) desc, " +
                    "    q.last_redaction_date")
        .setFirstResult(offset)
        .setMaxResults(itemsOnPage)
        .unwrap(Query.class)
        .setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] strings) {

                Map<String, Object> map = new HashMap<>();

                for (int i = 0; i <= strings.length - 1; i++){
                    map.put(strings[i], tuple[i]);
                }

                QuestionViewDto questionViewDto = new QuestionViewDto();
                questionViewDto.setId(((BigInteger) map.get("id")).longValue());
                questionViewDto.setTitle((String) map.get("title"));
                questionViewDto.setAuthorId(((BigInteger) map.get("authorid")).longValue());
                questionViewDto.setAuthorReputation(((BigInteger) map.get("authorreputation")).longValue());
                questionViewDto.setAuthorName((String) map.get("authorname"));
                questionViewDto.setAuthorImage((String) map.get("authorimage"));
                questionViewDto.setDescription((String) map.get("description"));
                questionViewDto.setCountAnswer(((Number) map.get("countanswer")).intValue());
                questionViewDto.setCountValuable(((BigInteger) map.get("countvaluable")).intValue());
                questionViewDto.setPersistDateTime(((Timestamp) map.get("persistdatetime")).toLocalDateTime());
                questionViewDto.setLastUpdateDateTime(((Timestamp) map.get("lastupdatedatetime")).toLocalDateTime());
                return questionViewDto;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        })
        .getResultList();

        return list;
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count (q) from Question as q")
                .getSingleResult();
    }
}
