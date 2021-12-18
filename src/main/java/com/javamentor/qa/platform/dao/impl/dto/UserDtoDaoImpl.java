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
    public List<UserDto> findUserDto(Long id) {
        return entityManager.createQuery("SELECT NEW com.javamentor.qa.platform.models.dto.UserDto(" +
                        "rep.author.id, " +
                        "rep.author.email, " +
                        "rep.author.fullName, " +
                        "rep.author.imageLink, " +
                        "rep.author.city," +
                        "rep.count) FROM Reputation rep where rep.author.id=:id")
                .setParameter("id", id)
                .getResultList();
    }
}
