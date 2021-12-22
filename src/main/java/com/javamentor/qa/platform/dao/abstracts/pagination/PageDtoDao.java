package com.javamentor.qa.platform.dao.abstracts.pagination;

import com.javamentor.qa.platform.models.entity.pagination.PaginationData;

import java.util.List;

public interface PageDtoDao<T> {

    List<T> getPaginationItems(PaginationData properties);

    Long getTotalResultCount();

}
