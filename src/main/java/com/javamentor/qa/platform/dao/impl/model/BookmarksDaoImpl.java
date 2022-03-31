package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookmarksDao;
import com.javamentor.qa.platform.models.entity.BookMarks;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BookmarksDaoImpl extends ReadWriteDaoImpl<BookMarks, Long> implements BookmarksDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookMarks> getBookmarksByUserAndQuestion(Long userId, Long questionId) {
        return entityManager
                .createQuery("select b from BookMarks b where b.user.id =:userId and b.question.id =:questionId", BookMarks.class)
                .setParameter("userId", userId)
                .setParameter("questionId", questionId)
                .getResultList();
    }

    @Override
    public void persist(BookMarks bookMarks) {
        super.persist(bookMarks);
    }
}
