package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.dto.user.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("userPageDtoDaoByVoteImpl")
public class UserPageDtoDaoByVoteImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    //private Long id;
    //    private String email;
    //    private String fullName;
    //    private String linkImage;
    //    private String city;
    //    private int reputation;

    @Override
    public List<UserDto> getPaginationItems(PaginationData properties) {
        List<UserDto> userDtoList = entityManager.createQuery(
                "SELECT u.email, u.fullName, u.imageLink, u.city, COUNT(r.count) as reputation " +
                        "" +
                        "FROM User u, Reputation r, VoteAnswer vA, VoteQuestion vQ WHERE u.id = r.author.id " +
                        "ORDER BY reputation"
        ).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(TagDto.class))
                .getResultList();
        return null;
    }

    @Override
    public Long getTotalResultCount() {
        return entityManager.createQuery(
                "SELECT COUNT(u.id) FROM User u",
                Long.class
        ).getSingleResult();
    }
}
