package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.AnswerDTO;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoService extends PageDtoService<AnswerDTO> {
    Optional<AnswerDTO> getAnswerDtoById(Long id);

    List<AnswerDTO> getAllAnswerDtoByQuestionId(Long questionId);
}
