package com.qa.platform.dao.abstracts.dto;

import com.qa.platform.models.dto.AnswerDTO;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoDao {

    Optional<AnswerDTO> getAnswerDtoById(Long id);

    List<AnswerDTO> getAllAnswerDtoByQuestionId(Long questionId);
}
