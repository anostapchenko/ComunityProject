package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.BookMarksDtoDao;
import com.javamentor.qa.platform.models.dto.BookMarksDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BookMarksDtoDaoImpl implements BookMarksDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookMarksDto> getAllBookMarksInUserProfile(Long id) {
        return (List<BookMarksDto>) entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.BookMarksDto (" +
                        "q.id, q.title ," +
                        "(select count(a.id) from Answer a where a.question.id = q.id)," +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id = q.id)," +
                        "(select count(qv.id) FROM QuestionViewed qv where qv.question.id = q.id)," +
                        "q.persistDateTime) " +
                        "from Question q where q.isDeleted = false and q.user.id=:id")
                .setParameter("id", id)
                .getResultList();
    }
}
