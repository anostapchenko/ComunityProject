package com.javamentor.qa.platform.dao.impl.pagination.transformer;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import java.time.LocalDateTime;

public class QuestionPageDtoResultTransformerCheckNPE extends QuestionPageDtoResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] strings) {
        QuestionViewDto questionViewDto = new QuestionViewDto();
        questionViewDto.setId((Long) tuple[0]);
        questionViewDto.setTitle((String) tuple[1]);
        questionViewDto.setAuthorId((Long) tuple[2]);
        questionViewDto.setAuthorName((String) tuple[3]);
        questionViewDto.setAuthorImage((String) tuple[4]);
        questionViewDto.setDescription((String) tuple[5]);
        questionViewDto.setPersistDateTime((LocalDateTime) tuple[6]);
        questionViewDto.setLastUpdateDateTime((LocalDateTime) tuple[7]);
        questionViewDto.setAuthorReputation((tuple[8] == null ? 0l : ((Number) tuple[8]).intValue()));
        questionViewDto.setCountAnswer((tuple[9] == null ? 0 : ((Number) tuple[9]).intValue()));
        questionViewDto.setCountValuable((tuple[10] == null ? 0 : ((Number) tuple[10]).intValue()));
        return questionViewDto;
    }
}
