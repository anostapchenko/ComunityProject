package com.qa.platform.service.impl.dto;

import com.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.qa.platform.exception.NoSuchDaoException;
import com.qa.platform.models.dto.PageDTO;
import com.qa.platform.models.entity.pagination.PaginationData;

import java.util.Map;

public abstract class DtoServiceImpl<T> {

    private final Map<String, PageDtoDao<T>> daoMap;

    public DtoServiceImpl(Map<String, PageDtoDao<T>> daoMap) {
        this.daoMap = daoMap;
    }

    public PageDTO<T> getPageDto(PaginationData properties) {
        if (!daoMap.containsKey(properties.getDaoName())) {
            throw new NoSuchDaoException("There is no dao with name: " + properties.getDaoName());
        }
        PageDtoDao<T> currentDao = daoMap.get(properties.getDaoName());
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setCurrentPageNumber(properties.getCurrentPage());
        pageDTO.setItems(currentDao.getPaginationItems(properties));
        pageDTO.setItemsOnPage(pageDTO.getItems().size());
        pageDTO.setTotalResultCount(currentDao.getTotalResultCount(properties.getProps()));
        pageDTO.setTotalPageCount((int) Math.ceil((double) pageDTO.getTotalResultCount() / properties.getItemsOnPage()));
        return pageDTO;
    }
}
