package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserProfileQuestionDto> getAllUserProfileQuestionDtoById(Long id) {
        return entityManager.createQuery("select  new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                "q.id,q.title," +
                "coalesce((select count(a.id) from Answer a where a.question.id=q.id),0),q.persistDateTime ) " +
                "from Question q where q.user.id=:id")
                .setParameter("id",id)
                .getResultList();
    }

    @Override
    public Optional<UserDto> findUserDto(Long id) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("SELECT NEW com.javamentor.qa.platform.models.dto.UserDto(" +
                        "u.id," +
                        "u.email," +
                        "u.fullName," +
                        "u.imageLink," +
                        "u.city," +
                        "(select sum(r.count) from Reputation r where r.author.id=u.id)) FROM User u where u.id=:id and u.isDeleted=false", UserDto.class)
                .setParameter("id", id));
    }

    @Override
    public List<UserProfileQuestionDto> getAllUserProfileQuestionDtoByUserIdWhereQuestionIsDeleted(Long id) {
        return (List<UserProfileQuestionDto>) entityManager.createQuery("select " +
                        "new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto (" +
                        "q.id," +
                        "q.title ," +
                        "coalesce((select count(a.id) from Answer a where a.question.id = q.id),0), " +
                        "q.persistDateTime) " +
                        "from Question q  where q.isDeleted=true and q.user.id=:id")
                .setParameter("id", id)
                .getResultList();
    }
}
