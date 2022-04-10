package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.BookMarksDto;

import java.util.List;

public interface BookMarksDtoService {
    List<BookMarksDto> getAllBookMarksInUserProfile(Long id);
}
