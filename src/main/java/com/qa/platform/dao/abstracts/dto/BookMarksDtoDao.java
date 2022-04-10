package com.qa.platform.dao.abstracts.dto;

import com.qa.platform.models.dto.BookMarksDto;

import java.util.List;

public interface BookMarksDtoDao {
    List<BookMarksDto> getAllBookMarksInUserProfile(Long id);
}
