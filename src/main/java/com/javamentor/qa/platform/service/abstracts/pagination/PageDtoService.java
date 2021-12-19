package com.javamentor.qa.platform.service.abstracts.pagination;

import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;


public interface PageDtoService<T> {

    PageDTO<T> getPageDto(PaginationData properties, String daoName);

}
