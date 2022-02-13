package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDTO;
import java.util.Optional;

public interface AnswerDtoDao {

    Optional<AnswerDTO> getAnswerDtoById(Long id);

}
