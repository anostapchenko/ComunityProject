package com.qa.platform.dao.impl.model;

import com.qa.platform.dao.abstracts.model.UserDao;
import com.qa.platform.dao.util.SingleResultUtil;
import com.qa.platform.models.entity.user.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "User", key = "#email")
    public Optional<User> getWithRoleByEmail(String email) {
        String hql = "select u from User u join fetch u.role r where u.email = :email";
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    @CacheEvict(value = "User", key = "#user.email")
    public void update(User user) {
        super.update(user);
    }

    @Override
    @CacheEvict(value = "User", key = "#email")
    public void deleteById(String email) {
        entityManager
                .createQuery("update User u set u.isDeleted=true where u.email=:email")
                .setParameter("email", email)
                .executeUpdate();
    }

    @Override
    public void deleteById(Long id) {} // Не должен работать, оставляю пустым. Может бросать исключение?

    @Override
    @CacheEvict(value = "User", key = "#user.email")
    public void delete(User user) {super.delete(user);}

    @Override
    @CacheEvict(value = "User", key = "#username")
    public void changePassword(String password, String username) {
        entityManager
                .createQuery("update User u set u.password = :password where u.email = :username")
                .setParameter("password", password)
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    @Cacheable(value = "User", key = "#email")
    public boolean isUserExistByEmail(String email) {
        String hql = "select u from User u where u.email = :email";
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query).isPresent();
    }
}
