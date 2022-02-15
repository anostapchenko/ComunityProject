package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoService extends PageDtoService<AnswerDTO> {
    Optional<AnswerDTO> getAnswerDtoById(Long id);

    List<AnswerDTO> getAllAnswerDtoByQuestionId(Long questionId);
}
