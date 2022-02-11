package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AnswerDTO> getAnswerDtoById(Long id) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("SELECT " +
                        "new com.javamentor.qa.platform.models.dto.AnswerDTO(" +
                        "a.id, a.persistDateTime, a.htmlBody) " +
                        "from Answer as a " +
                        "where a.id = :id", AnswerDTO.class)
                        .setParameter("id", id));
    }
}
