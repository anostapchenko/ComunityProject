package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

import java.util.Optional;


public interface QuestionDtoService  {
    Optional<QuestionDto> getQuestionDtoServiceById(Long id);
}
