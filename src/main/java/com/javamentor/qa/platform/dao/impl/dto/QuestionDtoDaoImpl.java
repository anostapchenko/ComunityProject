package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.impl.pagination.transformer.QuestionDtoResultTransformer;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    public final TagDtoDao tagDtoDao;

    public QuestionDtoDaoImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<QuestionCommentDto> getQuestionIdComment(Long id) {
        return entityManager.createQuery(
                        "SELECT NEW com.javamentor.qa.platform.models.dto.question.QuestionCommentDto(" +
                                "c.comment.id," +
                                "c.question.id," +
                                "c.comment.lastUpdateDateTime," +
                                "c.comment.persistDateTime," +
                                "c.comment.text," +
                                "c.comment.user.id," +
                                "c.comment.user.imageLink," +
                                "(select sum(r.count) from Reputation r where r.author.id=c.id)) FROM CommentQuestion c where c.question.id=:id")
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Optional<QuestionDto> getQuestionDtoDaoById(Long id) {

        TypedQuery<QuestionDto> dto = entityManager.createQuery("select q.id, q.title, u.id," +
                        " u.fullName, u.imageLink, q.description, q.persistDateTime," +
                        " q.lastUpdateDateTime, (select sum(r.count) from Reputation r where r.author.id =u.id), " +
                        "(select count (a.id) from Question q JOIN Answer a ON a.question.id = q.id WHERE q.id =:id)," +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v JOIN Question " +
                        "q ON v.question.id = q.id where q.id =:id) from Question q JOIN q.user u WHERE q.id =:id")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoResultTransformer());
        return SingleResultUtil.getSingleResultOrNull(dto);
    }
}
