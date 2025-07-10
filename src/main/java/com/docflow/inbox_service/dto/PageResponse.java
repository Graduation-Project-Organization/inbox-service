package com.docflow.inbox_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private boolean hasNext;
    private long totalElements;
    private int totalPages;
}
