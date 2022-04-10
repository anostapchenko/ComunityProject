package com.qa.platform.dao.impl.dto;

import com.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.qa.platform.dao.util.SingleResultUtil;
import com.qa.platform.models.dto.AnswerDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AnswerDTO> getAnswerDtoById(Long id) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("SELECT new com.qa.platform.models.dto.AnswerDTO(" +
                        " a.id, a.user.id, (SELECT sum(r.count) FROM Reputation r where r.answer.user.id = a.user.id), " +
                        " a.question.id, a.htmlBody, a.persistDateTime, a.isHelpful, a.dateAcceptTime, " +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteAnswer v where v.answer.id = a.id)," +
                        " a.user.imageLink, a.user.nickname) " +
                        " FROM Answer as a" +
                        " WHERE a.id = :id", AnswerDTO.class)
                .setParameter("id", id));
    }

    @Override
    public List<AnswerDTO> getAllAnswerDtoByQuestionId(Long questionId) {
        return entityManager.createQuery("SELECT new com.qa.platform.models.dto.AnswerDTO(" +
                        " a.id, a.user.id, (SELECT sum(r.count) FROM Reputation r where r.answer.user.id = a.user.id), " +
                        " a.question.id, a.htmlBody, a.persistDateTime, a.isHelpful, a.dateAcceptTime, " +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteAnswer v where v.answer.id = a.id)," +
                        " a.user.imageLink, a.user.nickname) " +
                        " FROM Answer as a" +
                        " WHERE a.question.id = :id", AnswerDTO.class)
                .setParameter("id", questionId)
                .getResultList();
    }
}
