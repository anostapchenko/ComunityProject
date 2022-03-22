package com.javamentor.qa.platform.dao.impl.pagination.tagdto;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.question.TagViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository("TagPageDtoDaoAllTagsByNameImpl")
public class TagPageDtoDaoAllTagsByNameImpl implements PageDtoDao<TagViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        LocalDateTime currentDate = LocalDateTime.now();
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.question.TagViewDto " +
                                            "(t.id, t.name, t.persistDateTime, t.description, " +
                                            "(select count(distinct q.id) from t.questions q) as countQuestion, " +
                                            "(select count(distinct q.id) from t.questions q where q.persistDateTime <= :current and :countOneDay < q.persistDateTime) as questionCountOneDay, " +
                                            "(select count(distinct q.id) from t.questions q where q.persistDateTime <= :current and :countWeekDay < q.persistDateTime) as questionCountWeekDay) " +
                                            "from Tag t where t.name like concat('%',:filter,'%') order by t.name", TagViewDto.class)
                .setParameter("current", currentDate)
                .setParameter("countOneDay", currentDate.minusDays(1))
                .setParameter("countWeekDay", currentDate.minusDays(7))
                .setParameter("filter",properties.getProps().get("filter"))
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(t.id) from Tag t " +
                "where t.name like concat('%',:filter,'%')")
                .setParameter("filter",properties.get("filter"))
                .getSingleResult();
    }
}
