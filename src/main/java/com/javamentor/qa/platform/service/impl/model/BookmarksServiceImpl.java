package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookmarksDao;
import com.javamentor.qa.platform.models.entity.BookMarks;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.BookmarksService;
import com.javamentor.qa.platform.webapp.controllers.exceptions.AddBookmarkException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarksServiceImpl  extends ReadWriteServiceImpl<BookMarks, Long> implements BookmarksService {

    private final BookmarksDao bookmarksDao;

    public BookmarksServiceImpl(BookmarksDao bookmarksDao) {
        super(bookmarksDao);
        this.bookmarksDao = bookmarksDao;
    }

    @Override
    @Transactional
    public void addQuestionInBookmarks(User user, Question question) {
        if(!bookmarksDao.findBookmarksByUserAndQuestion(user.getId(), question.getId())){
            throw new AddBookmarkException("The bookmark has not been added");
        }
        BookMarks bookMarks = BookMarks.builder()
                .question(question)
                .user(user)
                .build();
        bookmarksDao.persist(bookMarks);
    }
}
