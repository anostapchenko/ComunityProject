package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    public QuestionDto getQuestionDtoDaoById(Long id) {

        Query dto = entityManager.createQuery("select q.id, q.title, u.id," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime, (select sum(r.count) from Reputation r where r.author.id =u.id), " +
                        "(select count (a.id) from Question q JOIN Answer a ON a.question.id = q.id WHERE q.id =:id)," +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v JOIN Question " +
                        "q ON v.question.id = q.id where q.id =:id) from Question q JOIN q.user u WHERE q.id =:id")
                .setParameter("id", id)
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
                        questionDto.setCountAnswer(((Number) tuple[9]).intValue());
                        questionDto.setCountValuable(((Number) tuple[10]).intValue());
                        return questionDto;
                    }
                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                });
        return (QuestionDto) dto.getSingleResult();

    }
}
