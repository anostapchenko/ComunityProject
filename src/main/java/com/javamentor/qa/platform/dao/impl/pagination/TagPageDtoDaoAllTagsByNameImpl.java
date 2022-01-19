package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("TagPageDtoDaoAllTagsByNameImpl")
public class TagPageDtoDaoAllTagsByNameImpl implements PageDtoDao<TagDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.question.TagDto (t.id, t.name, t.persistDateTime) from Tag t order by t.name", TagDto.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount() {
        return (Long) entityManager.createQuery("select count(t.id) from Tag t").getSingleResult();
    }
}
