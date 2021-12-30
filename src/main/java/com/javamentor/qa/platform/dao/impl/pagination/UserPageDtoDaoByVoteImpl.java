package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.user.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("UserPageDtoDaoByVoteImpl")
public class UserPageDtoDaoByVoteImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager.createQuery(
                "SELECT u.id, u.email, u.fullName, u.imageLink, u.city, " +
                        "SUM(CASE WHEN r.count = NULL THEN 0 ELSE r.count END) AS reputation," +
                        "SUM(CASE WHEN r.type = :voteAns OR r.type = :voteQuest THEN 1 ELSE 0 END) AS voteOrder " +
                        "FROM User u LEFT JOIN Reputation r ON r.author.id = u.id " +
                        "GROUP BY u.id ORDER BY voteOrder DESC, u.id ASC "
        )
                .setParameter("voteAns", ReputationType.VoteAnswer)
                .setParameter("voteQuest", ReputationType.VoteQuestion)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public UserDto transformTuple(Object[] objects, String[] strings) {
                        Long reputation = (Long)objects[5];
                        return new UserDto(
                                (Long)objects[0],
                                (String)objects[1],
                                (String)objects[2],
                                (String)objects[3],
                                (String)objects[4],
                                reputation.intValue());
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
                .getResultList();
    }

    @Override
    public Long getTotalResultCount() {
        return entityManager.createQuery(
                "SELECT COUNT(u.id) FROM User u",
                Long.class
        ).getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
