package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
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

        Query dto = entityManager.createQuery("select q.id, q.title, u.id, r.count," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime from Question q, Reputation r" +
                        " JOIN q.user u WHERE q.id =:id and r.author = q.user")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuple, String[] strings) {
                        QuestionDto questionDto = new QuestionDto();
                        questionDto.setId((Long) tuple[0]);
                        questionDto.setTitle((String) tuple[1]);
                        questionDto.setAuthorId((Long) tuple[2]);
                        questionDto.setAuthorReputation(((Number) tuple[3]).longValue());
                        questionDto.setAuthorName((String) tuple[4]);
                        questionDto.setAuthorImage((String) tuple[5]);
                        questionDto.setDescription((String) tuple[6]);
                        questionDto.setPersistDateTime((LocalDateTime) tuple[7]);
                        questionDto.setLastUpdateDateTime((LocalDateTime) tuple[8]);
//                        questionDto.setListTagDto((List<TagDto>) tuple[9]);
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
