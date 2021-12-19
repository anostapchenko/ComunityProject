package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserDto> findUserDto(Long id) {
        List<Long> sql = entityManager.createQuery("select SUM(r.count+r.sender) from Reputation r WHERE r.author.id=:id")
                .setParameter("id", id)
                .getResultList();

        return entityManager.createQuery("SELECT NEW com.javamentor.qa.platform.models.dto.UserDto(" +
                        "rep.author.id, " +
                        "rep.author.email, " +
                        "rep.author.fullName, " +
                        "rep.author.imageLink, " +
                        "rep.author.city," +
                        sql.get(0)+") FROM Reputation rep where rep.author.id=:id")
                .setParameter("id", id)
                .getResultList();
    }
}
