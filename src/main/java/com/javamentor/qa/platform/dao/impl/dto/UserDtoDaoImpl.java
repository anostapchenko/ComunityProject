package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserDto findUserDto(Long id) {
        List<Long> sql = entityManager.createQuery("select SUM(r.count) from Reputation r WHERE r.author.id=:id")
                .setParameter("id", id)
                .getResultList();

        return (UserDto) entityManager.createQuery("SELECT NEW com.javamentor.qa.platform.models.dto.UserDto(" +
                        "u.id," +
                        "u.email," +
                        "u.fullName," +
                        "u.imageLink," +
                        "u.city," +
                        sql.get(0)+") FROM User u  where u.id=:id")
                .setParameter("id", id)
                .getSingleResult();
    }
}
