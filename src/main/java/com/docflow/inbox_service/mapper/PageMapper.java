package com.docflow.inbox_service.mapper;

import org.mapstruct.Mapper;
import com.docflow.inbox_service.dto.PageResponse;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public abstract class PageMapper {
    // TODO: I don't understand generic syntax in this method
    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        // Set base pagination info
        response.setContent(page.getContent());
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setHasNext(page.hasNext());

        // Set additional totals information
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }
}
