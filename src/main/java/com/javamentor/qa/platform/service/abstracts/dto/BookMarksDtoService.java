package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.BookMarksDto;

import java.util.List;

public interface BookMarksDtoService {
    List<BookMarksDto> getAllBookMarksInUserProfile(Long id);
}
