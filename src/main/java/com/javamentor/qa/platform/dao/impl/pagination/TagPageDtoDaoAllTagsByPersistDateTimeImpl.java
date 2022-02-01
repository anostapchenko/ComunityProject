package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.question.TagViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("TagPageDtoDaoAllTagsByPersistDateTimeImpl")
public class TagPageDtoDaoAllTagsByPersistDateTimeImpl implements PageDtoDao<TagViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.question.TagViewDto " +
                                            "(t.id, t.name, t.persistDateTime) " +
                                            "from Tag t order by t.persistDateTime", TagViewDto.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(t.id) from Tag t").getSingleResult();
    }
}
