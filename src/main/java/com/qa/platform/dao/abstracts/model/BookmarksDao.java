package com.qa.platform.dao.abstracts.model;

import com.qa.platform.models.entity.BookMarks;

public interface BookmarksDao extends ReadWriteDao<BookMarks, Long> {

    boolean findBookmarksByUserAndQuestion(Long userId, Long questionId);

}
