package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.AnswerPageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnswerPageDtoDaoByBodyImpl implements AnswerPageDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerDTO> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        // Query
        List<Answer> listAnswer =  entityManager.createQuery("from Answer order by htmlBody", Answer.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
        // Converting Answer to AnswerDTO
        List<AnswerDTO> list = new ArrayList<>();
        AnswerConverter mapper = Mappers.getMapper(AnswerConverter.class);
        for (Answer paginationItem : listAnswer) {
            list.add(mapper.answerToAnswerDTO(paginationItem));
        }
        return list;
    }

    @Override
    public Long getTotalResultCount() {
        return (Long) entityManager.createQuery("select count(a.id) from Answer a").getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
