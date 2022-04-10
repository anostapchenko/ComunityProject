package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.PageDTO;
import com.qa.platform.models.entity.pagination.PaginationData;


public interface PageDtoService<T> {

    PageDTO<T> getPageDto(PaginationData properties);

}
