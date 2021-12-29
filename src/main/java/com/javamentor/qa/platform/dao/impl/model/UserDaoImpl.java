package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getWithRoleByEmail(String email) {
        String hql = "select u from User u join fetch u.role r where u.email = :email";
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Transactional
    @Override
    public int disableUserWithId(long id) {
        String hql = "update User u set u.isEnabled=false where u.id=:id";
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("id", id);
        return query.executeUpdate();
    }

}
