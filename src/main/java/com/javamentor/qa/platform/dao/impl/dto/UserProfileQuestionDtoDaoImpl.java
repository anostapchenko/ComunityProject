package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileQuestionDtoDao;
import com.javamentor.qa.platform.dao.impl.dto.transformer.UserProfileQuestionDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("UserProfileQuestionDtoDaoImpl")
public class UserProfileQuestionDtoDaoImpl implements UserProfileQuestionDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserProfileQuestionDto> getAllUserProfileQuestionDtoByEmailWhereQuestionIsDeleted(String email) {
        return entityManager.createQuery("select q," +
                        "coalesce((select count(a.id) from Answer a where a.question.id = q.id),0) as countAnswer " +
                        "from Question q where q.isDeleted=true and (select u.email from User u where q.user.id=u.id)=:email")
                .setParameter("email", email)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new UserProfileQuestionDtoResultTransformer())
                .getResultList();
    }
}
