package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;


public interface QuestionDtoService  {
    QuestionDto getQuestionDtoServiceById(Long id);
}
