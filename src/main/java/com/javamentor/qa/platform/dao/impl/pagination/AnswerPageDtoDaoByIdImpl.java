package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.AnswerPageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AnswerPageDtoDaoByIdImpl implements AnswerPageDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerDTO> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return (List<AnswerDTO>) entityManager
                .createQuery("select a.id, a.persistDateTime, a.htmlBody from Answer a order by a.id")
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] objects, String[] strings) {
                                return new AnswerDTO(
                                        ((Number) objects[0]).longValue(),
                                        (LocalDateTime) objects[1],
                                        (String) objects[2]
                                );
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                )
                .getResultList();
    }

    @Override
    public Long getTotalResultCount() {
        return (Long) entityManager.createQuery("select count(a.id) from Answer a")
                .getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
