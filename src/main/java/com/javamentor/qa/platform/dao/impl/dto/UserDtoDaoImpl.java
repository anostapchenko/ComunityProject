package com.javamentor.qa.platform.dao.impl.dto;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserDto findUserDto(Long id) {

        return (UserDto) entityManager.createQuery("SELECT NEW com.javamentor.qa.platform.models.dto.UserDto(" +
                        "u.id," +
                        "u.email," +
                        "u.fullName," +
                        "u.imageLink," +
                        "u.city," +
                        "(select sum(r.count) from Reputation r where r.author.id=u.id)) FROM User u where u.id=:id and u.isDeleted=false ")
                .setParameter("id", id)
                .getSingleResult();
    }
}
