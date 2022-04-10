package com.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {

    private int currentPageNumber;
    private int totalPageCount;
    private long totalResultCount;
    private List<T> items;
    private int itemsOnPage;




}
