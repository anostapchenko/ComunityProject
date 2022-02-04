package com.javamentor.qa.platform.dao.impl.pagination;


import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("UserPageDtoDaoAllUsersImpl")
public class UserPageDtoDaoAllUsersImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDto " +
                        "(u.id, u.email, u.fullName, u.imageLink, u.city, " +
                        "(select sum(r.count) from Reputation r where r.author.id=u.id)) " +
                        "from User u where u.isDeleted = false order by u.persistDateTime", UserDto.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(u.id) from User u" ).getSingleResult();
    }

}
