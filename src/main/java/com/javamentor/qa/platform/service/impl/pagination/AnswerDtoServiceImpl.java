package com.javamentor.qa.platform.service.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.AnswerPageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.AnswerPageDtoService;
import com.javamentor.qa.platform.service.abstracts.pagination.PageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnswerDtoServiceImpl implements AnswerPageDtoService {

    private Map<String, AnswerPageDtoDao> daoMap;

    @Autowired
    public AnswerDtoServiceImpl(List<AnswerPageDtoDao> daoList) {
        this.daoMap = daoList.stream()
                .collect(Collectors.toMap(AnswerPageDtoDao::toString, Function.identity()));
    }

    @Override
    public PageDTO<AnswerDTO> getPageDto(PaginationData properties, String daoName) {
        AnswerPageDtoDao currentDao = daoMap.get(daoName);
        PageDTO<AnswerDTO> pageDTO = new PageDTO<>();

        pageDTO.setCurrentPageNumber(properties.getCurrentPage());
        pageDTO.setItems(currentDao.getPaginationItems(properties));
        pageDTO.setItemsOnPage(pageDTO.getItems().size());
        pageDTO.setTotalResultCount(currentDao.getTotalResultCount());
        pageDTO.setTotalPageCount(pageDTO.getTotalResultCount() % properties.getItemsOnPage() != 0 ?
                (int) (pageDTO.getTotalResultCount() / properties.getItemsOnPage() + 1) :
                (int) (pageDTO.getTotalResultCount() / properties.getItemsOnPage()));

        return pageDTO;
    }
}
