package com.javamentor.qa.platform.dao.impl.pagination;


import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("UserPageDtoDaoAllUsersImpl")
public class UserPageDtoDaoAllUsersImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
//        return (List<UserDto>) entityManager
//                .createQuery("select u.id, u.email, u.fullName, u.imageLink, u.city, (select sum(r.count) from Reputation r where r.author.id=u.id) from User u where u.isDeleted=false order by u.persistDateTime")
//                .setFirstResult(offset)
//                .setMaxResults(itemsOnPage)
//                .unwrap(org.hibernate.query.Query.class)
//                .setResultTransformer(
//                        new ResultTransformer() {
//                            @Override
//                            public Object transformTuple(Object[] objects, String[] strings) {
////                                return new UserDto(
////                                        ((Number) objects[0]).longValue(),
////                                        (String) objects[1],
////                                        (String) objects[2],
////                                        (String) objects[3],
////                                        (String) objects[4],
////                                        ((Number) objects[5]).intValue()
////                                );
//                                return UserDto.builder()
//                                        .id((Long) objects[0])
//                                        .email((String) objects[1])
//                                        .fullName((String) objects[2])
//                                        .imageLink((String) objects[3])
//                                        .city((String) objects[4])
//                                        .reputation(((Integer) objects[5]))
//                                        .build();
//                            }
//
//                            @Override
//                            public List transformList(List list) {
//                                return list;
//                            }
//                        }
//                )
//                .getResultList();
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDto (u.id, u.email, u.fullName, u.imageLink, u.city, (select sum(r.count) from Reputation r where r.author.id=u.id)) from User u order by u.persistDateTime", UserDto.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();

    }

    @Override
    public Long getTotalResultCount() {
        return (Long) entityManager.createQuery("select count(u.id) from User u" ).getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
