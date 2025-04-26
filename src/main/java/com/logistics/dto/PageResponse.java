package com.logistics.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private PaginationMetadata pagination;

    @Data
    public static class PaginationMetadata {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(page.getContent());
        
        PaginationMetadata metadata = new PaginationMetadata();
        metadata.setPageNumber(page.getNumber());
        metadata.setPageSize(page.getSize());
        metadata.setTotalElements(page.getTotalElements());
        metadata.setTotalPages(page.getTotalPages());
        metadata.setFirst(page.isFirst());
        metadata.setLast(page.isLast());
        metadata.setHasNext(page.hasNext());
        metadata.setHasPrevious(page.hasPrevious());
        
        response.setPagination(metadata);
        return response;
    }
} 