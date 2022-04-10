package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.BookMarks;
import com.qa.platform.models.entity.question.Question;
import com.qa.platform.models.entity.user.User;

public interface BookmarksService extends ReadWriteService<BookMarks, Long> {

    void addQuestionInBookmarks(User user, Question question);

}
