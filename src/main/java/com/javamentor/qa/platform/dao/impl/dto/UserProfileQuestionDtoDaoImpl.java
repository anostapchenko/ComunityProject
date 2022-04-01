package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.impl.dto.transformer.UserProfileQuestionDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("UserProfileQuestionDtoDaoImpl")
public class UserProfileQuestionDtoDaoImpl  {
    @PersistenceContext
    private EntityManager entityManager;

    public List<UserProfileQuestionDto> getAllUserProfileQuestionDtoByUserIdWhereQuestionIsDeleted(Long id) {
        return (List<UserProfileQuestionDto>) entityManager.createQuery("select q.id,q.title ," +
                        "coalesce((select count(a.id) from Answer a where a.question.id = q.id),0) as countAnswer, " +
                        "q.persistDateTime from Question q  where q.isDeleted=true and q.user.id=:id")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new UserProfileQuestionDtoResultTransformer())
                .getResultList();
    }
}
